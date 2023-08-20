package org.erick.finance.repository;

import org.erick.finance.domain.SpendingCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpendingCheckRepository extends JpaRepository<SpendingCheck, Long> {

	@Query("SELECT sc FROM SpendingCheck sc WHERE sc.spending.id = :idSpending")
	SpendingCheck getSpendingCheckBySpendingId(Long idSpending);
	
}
