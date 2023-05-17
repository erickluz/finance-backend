package org.erick.finance.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.erick.finance.dto.ItemCategoryDTO;

public interface CustomSpendingRepository {
	
	public List<ItemCategoryDTO> getListSpendingCategoryPerDate(LocalDateTime initialDate, LocalDateTime finalDate, Short groupingType, Boolean isBudget);	
}
