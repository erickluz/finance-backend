package org.erick.finance.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetDTO {
	private Long id;
	private String date;
	private String formattedDate;
	private BigDecimal value;
}
