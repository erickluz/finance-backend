package org.erick.finance.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.erick.finance.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long>{

	@Query(value = "  SELECT b.value FROM Budget b "
			+ "		WHERE MONTH(b.date) = MONTH(DATE(?1)) "
			+ "		AND YEAR(b.date) = YEAR(DATE(?1)) ")
	BigDecimal getBudgetValueByDate(LocalDateTime date);
	
	@Query(value = "  SELECT b FROM Budget b "
			+ "		WHERE MONTH(b.date) = MONTH(DATE(?1)) "
			+ "		AND YEAR(b.date) = YEAR(DATE(?1)) ")
	Budget getBudgetByDate(LocalDateTime date);

	@Query("SELECT SUM(b.value) FROM Budget b "
	       + "	WHERE b.date BETWEEN :initialDate AND :finalDate ")
	BigDecimal getTotalBudgetValue(LocalDateTime initialDate, LocalDateTime finalDate);

}
