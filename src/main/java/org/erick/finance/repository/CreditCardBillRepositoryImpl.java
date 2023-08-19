package org.erick.finance.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.erick.finance.domain.CreditCardSpending;

public class CreditCardBillRepositoryImpl implements CustomCreditCardBillRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CreditCardSpending> getCreditCardSpendingByDueDateBill(LocalDateTime creditCardSpendingDate) {
		StringBuilder sql = new StringBuilder(); 
		
		sql.append("	SELECT ccs FROM CreditCardSpending ccs ");
		sql.append("	INNER JOIN ccs.creditCardBill ccb ");
		sql.append("	LEFT JOIN ccs.spendingCreditCardSpending sccs ");
		sql.append("	INNER JOIN ccb.card c  ");
		sql.append("	WHERE MONTH(ccb.dueDate) = MONTH(DATE(:creditCardSpendingDate)) ");
		sql.append("	AND YEAR(ccb.dueDate) = YEAR(DATE(:creditCardSpendingDate)) ");
		sql.append("	AND sccs.id IS NULL ");
		
		Query equery = entityManager.createQuery(sql.toString());
		equery.setParameter("creditCardSpendingDate", creditCardSpendingDate);
		
		return (List<CreditCardSpending>) equery.getResultList();
	}

}
