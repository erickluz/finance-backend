package org.erick.finance.repository;

import org.erick.finance.domain.SpendingCreditCardSpending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpendingCreditCardSpendingRepository extends JpaRepository<SpendingCreditCardSpending, Long>  {

}
