package org.erick.finance.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpendingCategoryDTO {
	
	private List<ItemCategoryDTO> itens = new ArrayList<>();

}
