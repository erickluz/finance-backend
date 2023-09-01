package org.erick.finance.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.erick.finance.domain.SpendingCheckMonth;
import org.erick.finance.domain.SpendingCreditCardSpending;
import org.erick.finance.dto.AssociationIdDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpendingCheckMonthRepository extends JpaRepository<SpendingCheckMonth, Long>, CustomSpendingCheckMonthRepository{

	@Query(value = 
			"	SELECT to_date(CONCAT('01/' , EXTRACT(MONTH FROM date) , '/' , EXTRACT(year FROM date)), 'DD/MM/YYYY') as date"
			+ "	FROM Spending s"
			+ "	WHERE to_char(s.date, 'YYYY-MM') NOT IN ("
			+ "	  SELECT "
			+ " 	 to_char(scm.date, 'YYYY-MM') "
			+ "  	FROM spending_check_month scm"
			+ " 	GROUP BY to_char(scm.date, 'YYYY-MM')"
			+ "	)"
			+ "	GROUP BY"
			+ "	to_date(CONCAT('01/' , EXTRACT(MONTH FROM date) , '/' , EXTRACT(year FROM date)), 'DD/MM/YYYY')"
			+ "ORDER BY date", nativeQuery = true)
	List<Date> getMissingMonthsSpendingCheckMonth();
	
//	@Query(value = 
//	 "	SELECT new org.erick.finance.dto.SpendingCheckAssociationDTO(s.id, s.name, s.date, sc.id, c1.isChkByFile, c1.issuer, s.value, "
//	+ "		ccs.id, ccs.description, ccs.date, sccs.id, c2.isChkByFile, c2.issuer, ccs.value, s.type) "
//	+ "	FROM Spending s "
//	+ "	LEFT JOIN s.spendingsCheck sc "
//	+ "	LEFT JOIN sc.spendingCheckMonth scm "
//	+ "	LEFT JOIN s.spendingsCreditCardSpending sccs "
//	+ "	LEFT JOIN sccs.creditCardSpending ccs "
//	+ "	LEFT JOIN ccs.creditCardBill ccb "
//	+ "	LEFT JOIN s.card c1 "
//	+ "	LEFT JOIN ccb.card c2 "
//	+ "	WHERE MONTH(s.date) = MONTH(DATE(:spendingsMonth)) AND YEAR(s.date) = YEAR(DATE(:spendingsMonth)) "
//	+ "	AND s.type IN (1, 3, 4) "
//	+ "	AND s.id NOT IN (	"
//	+ "		SELECT s2.id "
//	+ "		FROM Spending s2	"
//	+ "		INNER JOIN s2.spendingGroupAssociation sg	"
//	+ "		WHERE MONTH(s2.date) = MONTH(DATE(:spendingsMonth)) AND YEAR(s2.date) = YEAR(DATE(:spendingsMonth)) "
//	+ "		AND s2.type	IN (1, 3) "
//	+ "		AND sg.type	= 4 "
//	+ "	)		"
//	+ "	ORDER BY s.date, s.id ")
//	List<SpendingCheckAssociationDTO> getSpendingsCheckAssociation(LocalDateTime spendingsMonth);

	@Query("	SELECT sccs "
			+ "	FROM SpendingCreditCardSpending sccs "
			+ "	INNER JOIN sccs.spending s "
			+ "	INNER JOIN sccs.creditCardSpending ccs "
			+ "	WHERE s.id = :idSpending"
			+ "	AND ccs.id = :idCreditCardSpending	")
	SpendingCreditCardSpending findByIdSpendingAndIdCreditCardSpending(Long idSpending, Long idCreditCardSpending);

	@Query("	SELECT new org.erick.finance.dto.AssociationIdDTO(s.id, ccs.id)"
			+ "	FROM Spending s "
			+ "	INNER JOIN CreditCardSpending ccs ON ("
			+ "		DAY(s.date) = DAY(ccs.date) "
//			+ "		AND MONTH(s.date) = MONTH(ccs.date) "
			+ "		AND YEAR(s.date) = YEAR(ccs.date) "
			+ "		AND s.value = ccs.value	"
			+ "	)"
			+ "	INNER JOIN ccs.creditCardBill ccb "
			+ "	WHERE MONTH(s.date) = MONTH(DATE(:date)) "
			+ "	AND YEAR(s.date) = YEAR(DATE(:date)) "
			+ "	AND MONTH(ccb.dueDate) = MONTH(DATE(:dateBill)) "
			+ "	AND YEAR(ccb.dueDate) = YEAR(DATE(:dateBill)) "
			+ "	AND s.type IN (1, 3) "
			+ "	AND ccs.type = 1 ")
	List<AssociationIdDTO> autoAssociations(LocalDateTime date, LocalDateTime dateBill);

	@Query("	SELECT scm FROM SpendingCheckMonth scm "
			+ "	WHERE MONTH(scm.date) = MONTH(DATE(:date)) "
			+ "	AND YEAR(scm.date) = YEAR(DATE(:date))")
	SpendingCheckMonth findByDate(LocalDateTime date);

	@Transactional
	@Modifying
	@Query(value = 
			  " UPDATE spending_check_month scm "
			+ "	SET is_checked = true "
			+ "	WHERE ("
			+ "	("
			+ "		SELECT count(s1.id) "
			+ "		FROM spending s1"
			+ "		WHERE extract(month FROM s1.date) = extract(month FROM scm.date) AND extract(year FROM s1.date) = extract(year FROM scm.date)"
			+ "		AND s1.type in (1, 3, 4)"
			+ "		AND s1.id not in ("
			+ "			SELECT ss1.id FROM spending ss1"
			+ "			INNER JOIN spending sg1 on sg1.id = ss1.id_spending_group_association "
			+ "			WHERE sg1.type = 4"
			+ "			AND ss1.type in (1, 3)"
			+ "			AND extract(month FROM ss1.date) = extract(month FROM s1.date) AND extract(year FROM ss1.date) = extract(year FROM s1.date)"
			+ "			AND extract(month FROM ss1.date) = extract(month FROM s1.date) AND extract(year FROM ss1.date) = extract(year FROM s1.date)"
			+ "		)"
			+ "		GROUP BY extract(month FROM s1.date), extract(year FROM s1.date)"
			+ "	)"
			+ "="
			+ "	("
			+ "		("
			+ "			SELECT count(s2.id) "
			+ "			FROM spending s2"
			+ "			INNER JOIN spending_credit_card_spending sccs on sccs.id_spending = s2.id"
			+ "			WHERE extract(month FROM s2.date) = extract(month FROM scm.date) AND extract(year FROM s2.date) = extract(year FROM scm.date) "
			+ "			GROUP BY extract(month FROM s2.date), extract(year FROM s2.date)"
			+ "		 )"
			+ "		+"
			+ "		("
			+ "			SELECT count(s3.id) "
			+ "			FROM spending s3"
			+ "			INNER JOIN spending_check sc on sc.id_spending = s3.id"
			+ "			WHERE extract(month FROM s3.date) = extract(month FROM scm.date) AND extract(year FROM s3.date) = extract(year FROM scm.date)"
			+ "			GROUP BY extract(month FROM s3.date), extract(year FROM s3.date)"
			+ "		)"
			+ "	)"
			+ ")"
			+ "	AND scm.is_checked = false", nativeQuery = true)
	void updateCheckMonthStatus();
	
}
