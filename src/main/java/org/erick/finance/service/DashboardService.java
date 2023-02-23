package org.erick.finance.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.erick.finance.domain.BudgetChart;
import org.erick.finance.domain.Stats;
import org.erick.finance.dto.SpendingCategoryDTO;
import org.erick.finance.repository.RevenueRepository;
import org.erick.finance.repository.SpendingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
	
	@Autowired
	private RevenueRepository revenueRepository;
	@Autowired
	private SpendingRepository spendingRepository;
	@Autowired
	private SpendingService spendingService;
	
	private static final BigDecimal GOAL_MONEY = new BigDecimal(4000.00);
	
	public Stats getStats() {
		BigDecimal totalRevenue = revenueRepository.getTotalRevenueByMonth();
		BigDecimal totalSpendingByMonth = spendingRepository.getTotalSpendingByMonth();
		//BigDecimal balance = totalRevenue.subtract(totalSpendingByMonth).subtract(GOAL_MONEY);
		BigDecimal targetBudget = totalRevenue.subtract(GOAL_MONEY);
		BigDecimal percentageBudget = totalSpendingByMonth.multiply(new BigDecimal(100)).divide(targetBudget, 2, RoundingMode.HALF_DOWN);
		BigDecimal totalBalance = getTotalBalance();
		return new Stats(currencyFormat(totalRevenue.subtract(GOAL_MONEY)), currencyFormat(totalSpendingByMonth), percentageBudget + " %", currencyFormat(totalBalance));
	}

	private BigDecimal getTotalBalance() {
		BigDecimal totalSpending = spendingRepository.getTotalSpending();
		Integer countMonths = spendingRepository.getCountMonts();
		BigDecimal totalRevenue = revenueRepository.getTotalRevenue();
		totalRevenue = totalRevenue.subtract(GOAL_MONEY.multiply(BigDecimal.valueOf(countMonths.doubleValue())));
		return totalRevenue.subtract(totalSpending);
	}
	
	public BudgetChart getBudgetChart() {
		LocalDateTime initialDate = spendingRepository.findTopByOrderByDateAsc().getDate();
		LocalDateTime finalDate = spendingRepository.findTopByOrderByDateDesc().getDate();
		List<Double> spendings = spendingRepository.getTotalSpendingPerMonth(initialDate, finalDate)
				.stream()
				.map(s -> s.doubleValue())
				.collect(Collectors.toList());
		List<Double> budgets = revenueRepository.getTotalRevenuePerMonth(initialDate, finalDate)
				.stream()
				.map(s -> s.doubleValue() - GOAL_MONEY.doubleValue())
				.collect(Collectors.toList());
		List<String> dates = spendingService.getListDatesSpending();
		return new BudgetChart(spendings, budgets, dates);
	}
	
	@SuppressWarnings("static-access")
	public static String currencyFormat(BigDecimal n) {
	    return NumberFormat.getInstance(new Locale("pt", "BR")).getCurrencyInstance().format(n);
	}
	
	public SpendingCategoryDTO getSpendingCategoryChart() {
		return new SpendingCategoryDTO(spendingRepository.getListSpendingCategory());
	}
	
	public SpendingCategoryDTO getSpendingCategoryChartPerDate(String initialDate, String finalDate) {
		LocalDateTime lInitialDate = LocalDateTime.parse(initialDate + " 00:00:00",DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		LocalDateTime lFinalDate = LocalDateTime.parse(finalDate + " 00:00:00",DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		lInitialDate = lInitialDate.withDayOfMonth(1);
		LocalDate lFinal = lFinalDate.toLocalDate(); 
		lFinalDate = lFinalDate.withDayOfMonth(lFinalDate.getMonth().length(lFinal.isLeapYear()));
		return new SpendingCategoryDTO(spendingRepository.getListSpendingCategoryPerDate(lInitialDate, lFinalDate));
	}

}
