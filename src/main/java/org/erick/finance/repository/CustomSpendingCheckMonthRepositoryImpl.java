package org.erick.finance.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.erick.finance.dto.SpendingCheckAssociationDTO;

public class CustomSpendingCheckMonthRepositoryImpl implements CustomSpendingCheckMonthRepository {
	
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<SpendingCheckAssociationDTO> getSpendingsCheckAssociation(LocalDateTime spendingsMonth, String association, String associable) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("	 	SELECT new org.erick.finance.dto.SpendingCheckAssociationDTO(s.id, s.name, s.date, sc.id, c1.isChkByFile, c1.issuer, s.value, "
				+ " 		ccs.id, ccs.description, ccs.date, sccs.id, c2.isChkByFile, c2.issuer, ccs.value, s.type) "
				+ " 	FROM Spending s "
				+ " 	LEFT JOIN s.spendingsCheck sc "
				+ " 	LEFT JOIN sc.spendingCheckMonth scm "
				+ " 	" + isLeftOrInner(association) + " JOIN s.spendingsCreditCardSpending sccs "
				+ " 	" + isLeftOrInner(association) + " JOIN sccs.creditCardSpending ccs "
				+ " 	LEFT JOIN ccs.creditCardBill ccb "
				+ " 	LEFT JOIN s.card c1 "
				+ " 	LEFT JOIN ccb.card c2 "
				+ " 	WHERE MONTH(s.date) = MONTH(DATE(:spendingsMonth)) AND YEAR(s.date) = YEAR(DATE(:spendingsMonth)) "
				+ " 	AND s.type IN (1, 3, 4) "
				+ " 	AND s.id NOT IN (	"
				+ " 		SELECT s2.id "
				+ " 		FROM Spending s2	"
				+ " 		INNER JOIN s2.spendingGroupAssociation sg	"
				+ " 		WHERE MONTH(s2.date) = MONTH(DATE(:spendingsMonth)) AND YEAR(s2.date) = YEAR(DATE(:spendingsMonth)) "
				+ " 		AND s2.type	IN (1, 3) "
				+ " 		AND sg.type	= 4 "
				+ " 	)		"
				+ filterAssociable(associable)
				+ filterAssociation(association)
				+ " 	ORDER BY s.date, s.id ");
		Query equery = entityManager.createQuery(sql.toString());
		
		equery.setParameter("spendingsMonth", spendingsMonth);

			
		return (List<SpendingCheckAssociationDTO>) equery.getResultList();
				
	}

	private String filterAssociation(String association) {
		String join = "";
		if (association.equals("Unassociated")) {
			join = " 	AND sc.id IS NULL AND sccs.id IS NULL ";
		}
		return join;
	}

	private String isLeftOrInner(String association) {
		String join = "LEFT";
		if (association.equals("Associated")) {
			join = "INNER";
		}
		return join;
	}

	private String filterAssociable(String associable) {
		String filter = "";
		if (associable.equals("Is Associable")) {
			filter = " 	AND c1.isChkByFile = true";
		} else if (associable.equals("No Associable")) {
			filter = " 	AND (c1.isChkByFile = false OR c1.isChkByFile IS NULL)";
		}
		return filter;
	}
}

