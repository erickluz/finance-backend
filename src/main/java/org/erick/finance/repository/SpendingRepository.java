package org.erick.finance.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.erick.finance.domain.Spending;
import org.erick.finance.dto.ItemCategoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpendingRepository extends JpaRepository<Spending, Long>{
	
	@Query("SELECT SUM(coalesce(s.value, 0.0)) "
			+ "		FROM Spending s "
			+ "		WHERE MONTH(s.date) = MONTH(current_date)"
			+ "		AND s.type <> :groupingType ")
	public BigDecimal getTotalSpendingByMonth(Short groupingType);
	
	@Query("SELECT SUM(s.value) "
			+ "		FROM  Spending s "
			+ "		WHERE s.date <= :date "
			+ "		AND s.type <> :groupingType ")
	public BigDecimal getTotalSpending(LocalDateTime date, Short groupingType);

	public Spending findTopByOrderByDateAsc();
	public Spending findTopByOrderByDateDesc();
	
	@Query("SELECT s "
			+ " FROM Spending s "
			+ " WHERE MONTH(s.date) = :month "
			+ "	AND s.type <> :groupingType ")
	public List<Spending> listByMonth(int month, Short groupingType);

	@Query("SELECT SUM(s.value), MONTH(s.date), YEAR(s.date) "
			+ " FROM Spending s "
			+ " WHERE s.date BETWEEN :initialDate AND :finalDate "
			+ "	AND s.type <> :groupingType "
			+ " GROUP BY MONTH(s.date), YEAR(s.date) "
			+ " ORDER BY YEAR(s.date), MONTH(s.date) ")
	public List<BigDecimal> getTotalSpendingPerMonth(LocalDateTime initialDate, LocalDateTime finalDate, Short groupingType);
	
	@Query("SELECT DISTINCT MONTH(s.date), YEAR(s.date) "
			+ "FROM Spending s "
			+ "	WHERE s.type <> :groupingType "
			+ "GROUP BY MONTH(s.date), YEAR(s.date) "
			+ "ORDER BY YEAR(s.date), MONTH(s.date) ")
	public List<Integer> getListSpending(Short groupingType);
	
	@Query("SELECT new org.erick.finance.dto.ItemCategoryDTO(SUM(s.value), c.name) "
			+ "FROM Spending s "
			+ "INNER JOIN s.category c "
			+ "WHERE MONTH(s.date) = MONTH(current_date) "
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
	public Integer getCountMonts();

}
