package org.erick.finance.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalsStats {
	private String totalRevenue;
	private String totalSpending;
	private String totalGoalMoney;
	private String totalBudgetBalance;
	private String totalBalance;
}