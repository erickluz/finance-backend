package org.erick.finance.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stats {
	private String totalRevenue;
	private String totalSpending;
	private String budgetPercent;
	private String balance;
}
