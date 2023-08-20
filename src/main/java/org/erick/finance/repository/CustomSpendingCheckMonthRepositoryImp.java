package org.erick.finance.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.erick.finance.domain.TypeSpending;
import org.erick.finance.dto.SpendingCheckAssociationDTO;

public class CustomSpendingCheckMonthRepositoryImp implements CustomSpendingCheckMonthRepository {
	
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<SpendingCheckAssociationDTO> getSpendingsCheckAssociation(LocalDateTime spendingsMonth) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("	SELECT new org.erick.finance.dto.SpendingCheckAssociationDTO(s.id, s.name, s.date, sc.id, c1.isChkByFile, c1.issuer, s.value, ");
		sql.append("		ccs.id, ccs.description, ccs.date, sccs.id, c2.isChkByFile, c2.issuer, ccs.value) ");
		sql.append("	FROM Spending s ");
		sql.append("	LEFT JOIN s.spendingsCheck sc ");
		sql.append("	LEFT JOIN sc.spendingCheckMonth scm ");
		sql.append("	LEFT JOIN s.spendingsCreditCardSpending sccs ");
		sql.append("	LEFT JOIN sccs.creditCardSpending ccs ");
		sql.append("	LEFT JOIN ccs.creditCardBill ccb ");
		sql.append("	LEFT JOIN s.card c1 ");
		sql.append("	LEFT JOIN ccb.card c2 ");
		sql.append("	WHERE MONTH(s.date) = MONTH(DATE(:spendingsMonth)) AND  YEAR(s.date) = YEAR(DATE(:spendingsMonth)) ");
		sql.append("	AND s.id NOT IN (	");
		sql.append("		SELECT s2.id ");
		sql.append("		FROM Spending s2	");
		sql.append("		INNER JOIN s2.spendingGroup sg	");
		sql.append("		WHERE MONTH(s.date) = MONTH(DATE(:spendingsMonth)) AND  YEAR(s.date) = YEAR(DATE(:spendingsMonth)) ");
		sql.append("		AND s2.type	= " + TypeSpending.NORMAL.getCode());
		sql.append("		AND sg.type	= " + TypeSpending.GROUP_ASSOCIATION.getCode());
		sql.append("	)		");
		sql.append("	ORDER BY s.date, s.id ");
		Query equery = entityManager.createQuery(sql.toString());
		
		equery.setParameter("spendingsMonth", spendingsMonth);

			
		return (List<SpendingCheckAssociationDTO>) equery.getResultList();
				
	}
}

