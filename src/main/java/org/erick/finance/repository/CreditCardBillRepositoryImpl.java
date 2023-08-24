package org.erick.finance.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.erick.finance.domain.CreditCardSpending;
import org.erick.finance.domain.CreditCardSpendingType;

public class CreditCardBillRepositoryImpl implements CustomCreditCardBillRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CreditCardSpending> getCreditCardSpendingByDueDateBill(LocalDateTime creditCardSpendingDate, String association) {
		StringBuilder sql = new StringBuilder(); 
		
		sql.append("	SELECT ccs FROM CreditCardSpending ccs ");
		sql.append("	INNER JOIN ccs.creditCardBill ccb ");
		sql.append("	LEFT JOIN ccs.spendingCreditCardSpending sccs ");
		sql.append("	INNER JOIN ccb.card c  ");
		sql.append("	WHERE MONTH(ccb.dueDate) = MONTH(DATE(:creditCardSpendingDate)) ");
		sql.append("	AND YEAR(ccb.dueDate) = YEAR(DATE(:creditCardSpendingDate)) ");
		sql.append("	AND sccs.id IS NULL ");
		sql.append("	AND ccs.type =  " + CreditCardSpendingType.NORMAL.getCode());
		sql.append(filterAssociation(association));
		sql.append("	AND ccs.id NOT IN ( ");
		sql.append("		SELECT ccs2.id FROM CreditCardSpending ccs2	");
		sql.append("		INNER JOIN ccs2.creditCardSpendingGrouping ccs2g ");
		sql.append("		INNER JOIN ccs2.creditCardBill ccb2 ");
		sql.append("		WHERE MONTH(ccb2.dueDate) = MONTH(DATE(:creditCardSpendingDate)) ");
		sql.append("		AND YEAR(ccb2.dueDate) = YEAR(DATE(:creditCardSpendingDate)) ");
		sql.append("	)	");
		sql.append("	ORDER BY ccs.date ");
		
		Query equery = entityManager.createQuery(sql.toString());
		equery.setParameter("creditCardSpendingDate", creditCardSpendingDate);
		
		return (List<CreditCardSpending>) equery.getResultList();
	}

	private String filterAssociation(String association) {
		String filter = " ";
		if (association.equals("Unassociated")) {
			filter = "	AND ccs.isJustified IS NOT TRUE ";
		}
		return filter;
	}

}
