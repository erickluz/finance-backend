package org.erick.finance.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChartSpendingDayDTO {
	private List<BigDecimal> values = new ArrayList<>();
	private List<String> days = new ArrayList<>();	
}
