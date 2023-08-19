package org.erick.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpendingCheckDTO {
	private Long id;
	private Long idSpending;
	private Long idSpendingCheckMonth;
	private String note;
}
