package org.erick.finance.repository;

import java.time.LocalDateTime;

import org.erick.finance.domain.CreditCardBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardBillRepository extends JpaRepository<CreditCardBill, Long>, CustomCreditCardBillRepository{

	@Query("SELECT c FROM CreditCardBill c "
			+ "	WHERE c.dueDate = :dueDate "
			+ "	AND c.card.id = :idCard ")
	CreditCardBill findByDueDateAndCreditCard(LocalDateTime dueDate, Long idCard);

}
