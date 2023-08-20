package org.erick.finance.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.erick.finance.dto.SpendingCheckAssociationDTO;

public interface CustomSpendingCheckMonthRepository {
	List<SpendingCheckAssociationDTO> getSpendingsCheckAssociation(LocalDateTime spendingsMonth);
}
