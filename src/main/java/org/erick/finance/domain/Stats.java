package org.erick.finance.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stats {
	private String revenueMonth;
	private String spendingMonth;
	private String balanceMonth;
	private String balanceGoalMonth;
	private String budgetMoney;
	private String goalMoney;
	private String budgetPercentage;
	private String totalBalance;
}
