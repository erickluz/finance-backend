package org.erick.finance.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.erick.finance.domain.CreditCardSpending;

public interface CustomCreditCardBillRepository {

	List<CreditCardSpending> getCreditCardSpendingByDueDateBill(LocalDateTime creditCardSpendingDate);
}
