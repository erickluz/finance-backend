package org.erick.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpendingCheckMonthDTO {
	private String id;
	private String nameMonth;
	private String date;
	private Boolean isCheck;

}
