package org.erick.finance.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stats {
	private String monthRevenue;
	private String monthSpending;
	private String monthBalance;
	private String monthBalanceGoal;
	private String monthBudgetMoney;
	private String monthGoalMoney;
	private String monthBudgetPercentage;
	private String totalRevenue;
	private String totalSpending;
	private String totalGoalMoney;
	private String totalBudgetBalance;
	private String totalBalance;
}