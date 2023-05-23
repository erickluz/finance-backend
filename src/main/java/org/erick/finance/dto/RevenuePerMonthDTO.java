package org.erick.finance.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenuePerMonthDTO {
	private BigDecimal value;
	private Integer month;
	private Integer year;
	private LocalDateTime date;

}
