package org.erick.finance.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.erick.finance.dto.ItemCategoryDTO;

public class CustomSpendingRepositoryImpl implements CustomSpendingRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemCategoryDTO> getListSpendingCategoryPerDate(LocalDateTime initialDate, LocalDateTime finalDate, Short groupingType, Boolean isBudget) {
		StringBuilder sql = new StringBuilder(); 
				
		sql.append("SELECT new org.erick.finance.dto.ItemCategoryDTO(SUM(s.value), ca.name) ");
		sql.append("	FROM Spending s ");
		sql.append("	INNER JOIN s.category ca ");
		sql.append("	LEFT JOIN s.card c ");
		sql.append("	WHERE s.date BETWEEN :initialDate AND :finalDate ");
		sql.append("	AND s.type <> :groupingType ");
		sql.append("	AND c.type <> 4 ");
		if (isBudget != null) {
			sql.append(isBudget(isBudget));
		}
		sql.append(" GROUP BY ca.name ");

		Query equery = entityManager.createQuery(sql.toString());
		equery.setParameter("initialDate", initialDate);
		equery.setParameter("finalDate", finalDate);
		equery.setParameter("groupingType", groupingType);	
		
		return (List<ItemCategoryDTO>) equery.getResultList();
	}

	private String isBudget(Boolean isBudget) {
		String query;
		if (isBudget) {
			query = "	AND ((c.id IS NOT NULL AND c.isBudget = TRUE) OR (c.id IS NULL)) ";
		} else {
			query = "	AND (c.id IS NOT NULL AND (c.isBudget = FALSE OR c.isBudget IS NULL)) ";
		}
		return query;
	}


}
