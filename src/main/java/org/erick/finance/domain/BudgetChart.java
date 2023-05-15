package org.erick.finance.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetChart {
	private List<BigDecimal> spendingsMonth = new ArrayList<>();
	private List<BigDecimal> budgetsMonth = new ArrayList<>();
	private List<BigDecimal> percentBudget = new ArrayList<>();
	private List<String> months = new ArrayList<>();
}
