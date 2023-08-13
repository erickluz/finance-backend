package org.erick.finance.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.erick.finance.domain.Revenue;
import org.erick.finance.dto.RevenuePerMonthDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long>{

	@Query("SELECT coalesce(SUM(coalesce(r.value, 0.0)), 0.0) FROM Revenue r "
			+ "	WHERE MONTH(r.date) = MONTH(DATE(:date)) "
			+ "	AND YEAR(r.date) = YEAR(DATE(:date))")
	BigDecimal getTotalRevenueByMonth(LocalDateTime date);

	@Query("SELECT new org.erick.finance.dto.RevenuePerMonthDTO(SUM(r.value), MONTH(r.date), YEAR(r.date), r.date) "
			+ " FROM Revenue r "
			+ " WHERE r.date BETWEEN :initialDate AND :finalDate "
			+ " GROUP BY MONTH(r.date), YEAR(r.date), r.date "
			+ " ORDER BY YEAR(r.date), MONTH(r.date), r.date ")
	List<RevenuePerMonthDTO> getTotalRevenuePerMonth(LocalDateTime initialDate, LocalDateTime finalDate);
	
	@Query("SELECT "
			+ " SUM(r.value) "
			+ " FROM Revenue r "
			+ " WHERE r.date BETWEEN :initialDate AND :finalDate ")
	BigDecimal getTotalRevenue(LocalDateTime initialDate, LocalDateTime finalDate);
	
}
