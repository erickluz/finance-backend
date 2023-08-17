package org.erick.finance.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.erick.finance.domain.CreditCardSpending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardSpendingRepository extends JpaRepository<CreditCardSpending, Long>{

	@Query("	SELECT ccs FROM CreditCardSpending ccs "
			+ " WHERE ccs.description = :description"
			+ "	AND ccs.date = :date"
			+ "	AND ccs.part = :part"
			+ "	AND ccs.creditCardBill.id = :id "
			+ "	AND ccs.value = :value ")
	CreditCardSpending findByDescriptionAndDateAndPartAndCreditCardBill(String description, LocalDateTime date,
			String part, Long id, BigDecimal value);


}
