package org.erick.finance.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetChart {
	private List<Double> spendingsMonth = new ArrayList<>();
	private List<Double> budgetsMonth = new ArrayList<>();
	private List<String> months = new ArrayList<>();
}
