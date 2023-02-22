package org.erick.finance.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCategoryDTO {
	private Double value;
	private String name;
	
	public ItemCategoryDTO(BigDecimal value, String name) {
		this.value = value.doubleValue();
		this.name = name;
	}

}
