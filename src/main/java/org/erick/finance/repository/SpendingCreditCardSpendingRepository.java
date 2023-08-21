package org.erick.finance.repository;

import org.erick.finance.domain.SpendingCreditCardSpending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpendingCreditCardSpendingRepository extends JpaRepository<SpendingCreditCardSpending, Long>  {

	@Query("	SELECT sccs FROM SpendingCreditCardSpending sccs "
			+ "	INNER JOIN sccs.spending s "
			+ "	INNER JOIN sccs.creditCardSpending ccs "
			+ "	WHERE s.id = :idSpending "
			+ "	AND ccs.id = :idCreditCardSpending ")
	SpendingCreditCardSpending findByIdSendingAndIdCreditCardSpending(Long idSpending, Long idCreditCardSpending);

}
