package org.erick.finance.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.erick.finance.domain.Spending;
import org.erick.finance.dto.ItemCategoryDTO;
import org.erick.finance.dto.SpendingDayDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpendingRepository extends JpaRepository<Spending, Long>, CustomSpendingRepository {
	
	@Query("SELECT coalesce(SUM(coalesce(s.value, 0.0)), 0.0) "
			+ "		FROM Spending s "
			+ "		LEFT JOIN s.card c "
			+ "		WHERE MONTH(s.date) = MONTH(DATE(:date)) AND YEAR(s.date) = YEAR(DATE(:date)) "
			+ "		AND s.type <> :groupingType "
			+ "		AND ((c.id IS NOT NULL AND c.isBudget = TRUE) OR (c.id IS NULL)) ")
	public BigDecimal getTotalSpendingByMonth(Short groupingType, LocalDateTime date);
	
	@Query("SELECT coalesce(SUM(s.value), 0.0) "
			+ "		FROM  Spending s "
			+ "		LEFT JOIN s.card c "
			+ "		WHERE s.date BETWEEN :initialDate AND :finalDate "
			+ "		AND s.type <> :groupingType "
			+ "		AND ((c.id IS NOT NULL AND c.isBudget = TRUE) OR (c.id IS NULL)) ")
	public BigDecimal getTotalSpending(LocalDateTime initialDate, LocalDateTime finalDate, Short groupingType);

	public Spending findTopByOrderByDateAsc();
	public Spending findTopByOrderByDateDesc();
	
	@Query("SELECT s "
			+ " FROM Spending s "
			+ "	LEFT JOIN FETCH s.card c "
			+ " WHERE MONTH(s.date) = :month "
			+ "	AND s.type <> :groupingType "
			+ "	ORDER BY s.id")
	public List<Spending> listByMonth(int month, Short groupingType);

	@Query("SELECT SUM(s.value), MONTH(s.date), YEAR(s.date) "
			+ " FROM Spending s "
			+ "	LEFT JOIN s.card c "
			+ " WHERE s.date BETWEEN :initialDate AND :finalDate "
			+ "	AND s.type <> :groupingType "
			+ " AND ((c.id IS NOT NULL AND c.isBudget = TRUE) OR (c.id IS NULL)) "
			+ " GROUP BY MONTH(s.date), YEAR(s.date) "
			+ " ORDER BY YEAR(s.date), MONTH(s.date) ")
	public List<BigDecimal> getTotalSpendingPerMonth(LocalDateTime initialDate, LocalDateTime finalDate, Short groupingType);
	
	@Query("SELECT DISTINCT MONTH(s.date), YEAR(s.date) "
			+ "FROM Spending s "
			+ " WHERE s.date BETWEEN :initialDate AND :finalDate "
			+ "	AND s.type <> :groupingType "
			+ "GROUP BY MONTH(s.date), YEAR(s.date) "
			+ "ORDER BY YEAR(s.date), MONTH(s.date) ")
	public List<Integer> getListSpending(Short groupingType, LocalDateTime initialDate, LocalDateTime finalDate);
	
	@Query("SELECT new org.erick.finance.dto.ItemCategoryDTO(SUM(s.value), c.name) "
			+ "FROM Spending s "
			+ "INNER JOIN s.category c "
			+ "WHERE MONTH(s.date) = MONTH(current_date) AND YEAR(s.date) = YEAR(current_date) "
			+ "	AND s.type <> :groupingType "
			+ "GROUP BY c.name ")
	public List<ItemCategoryDTO> getListSpendingCategory(Short groupingType);
	
	
	@Query("SELECT new org.erick.finance.dto.ItemCategoryDTO(SUM(s.value), c.name) "
			+ " FROM Spending s "
			+ " INNER JOIN s.category c "
			+ " WHERE s.date BETWEEN :initialDate AND :finalDate "
			+ "	AND s.type <> :groupingType "
			+ " GROUP BY c.name ")
	public List<ItemCategoryDTO> getListSpendingCategoryPerDate(LocalDateTime initialDate, LocalDateTime finalDate, Short groupingType);
	
	@Query(value = "select count(*) from ("
			+ "SELECT  extract(MONTH from s.date)"
			+ "						FROM Spending s "
			+ "						WHERE s.date <= current_date "
			+ "						GROUP BY extract(MONTH from s.date)	"
			+ ") as months", nativeQuery = true)
	public Integer getCountMonths();
	
	@Query("SELECT new org.erick.finance.dto.SpendingDayDTO(SUM(s.value), s.date) "
			+ "FROM Spending s "
			+ "WHERE MONTH(s.date) = MONTH(current_date) "
			+ "	AND YEAR(s.date) = YEAR(current_date) "
			+ "AND s.type IN (1, 3)	"
			+ "GROUP BY s.date "
			+ "ORDER BY s.date ASC ")
	public List<SpendingDayDTO> getListSpendingPerDay();

	@Query(" SELECT s.spendingsInsallments FROM Spending s "
			+ "	WHERE s.id = :id "
			+ "	ORDER BY s.date ASC ")
	public List<Spending> getFirstPartSpending(Long id);

}
