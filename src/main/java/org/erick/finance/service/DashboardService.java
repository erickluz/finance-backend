package org.erick.finance.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.erick.finance.domain.BudgetChart;
import org.erick.finance.domain.Stats;
import org.erick.finance.domain.TypeSpending;
import org.erick.finance.dto.ChartSpendingDayDTO;
import org.erick.finance.dto.SpendingCategoryDTO;
import org.erick.finance.dto.SpendingDayDTO;
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
		BigDecimal monthRevenue = revenueRepository.getTotalRevenueByMonth();
		BigDecimal monthSpending = spendingRepository.getTotalSpendingByMonth(TypeSpending.GROUPING.getCode());
		BigDecimal monthBalance = monthRevenue.subtract(monthSpending);
		BigDecimal monthBudgetMoney = monthRevenue.subtract(GOAL_MONEY);
		BigDecimal monthBalanceGoal = monthBudgetMoney.subtract(monthSpending); 
		BigDecimal monthBudgetPercentage = monthSpending.multiply(new BigDecimal(100)).divide(monthBudgetMoney, 2, RoundingMode.HALF_DOWN);
		int lastDay = LocalDate.now().withDayOfMonth(LocalDate.now().getMonth().length(LocalDate.now().isLeapYear())).getDayOfMonth();
		LocalDateTime finalDate = LocalDateTime.now().withDayOfMonth(lastDay);
		BigDecimal totalRevenue = revenueRepository.getTotalRevenue(finalDate);
		BigDecimal totalSpeding = spendingRepository.getTotalSpending(finalDate, TypeSpending.GROUPING.getCode());
		BigDecimal totalBalance = totalRevenue.subtract(totalSpeding);
		Integer countSpendingMonths = spendingRepository.getCountMonths();
		BigDecimal totalGoalMoney = getTotalGoalMoney(countSpendingMonths);
		BigDecimal totalBudgetBalance = getTotalBudgetBalance(countSpendingMonths);
		return new Stats(monthRevenue.toString(), monthSpending.toString(), monthBalance.toString(), monthBalanceGoal.toString(), 
				monthBudgetMoney.toString(), GOAL_MONEY.toString(), monthBudgetPercentage.toString(), totalRevenue.toString(), totalSpeding.toString(),
				totalGoalMoney.toString(),totalBudgetBalance.toString(), totalBalance.toString());
	}

	private BigDecimal getTotalGoalMoney(Integer countSpendingMonths) {
		return GOAL_MONEY.multiply(new BigDecimal(countSpendingMonths));
	}

	private BigDecimal getTotalBudgetBalance(Integer countSpendingMonths) {
		LocalDate lDate = LocalDate.now();
		LocalDateTime date = LocalDateTime.now().withDayOfMonth(LocalDateTime.now().getMonth().length(lDate.isLeapYear()));
		BigDecimal totalSpending = spendingRepository.getTotalSpending(date, TypeSpending.GROUPING.getCode());
		BigDecimal totalRevenue = revenueRepository.getTotalRevenue(date);
		totalRevenue = totalRevenue.subtract(GOAL_MONEY.multiply(BigDecimal.valueOf(countSpendingMonths.doubleValue())));
		return totalRevenue.subtract(totalSpending);
	}
	
	public BudgetChart getBudgetChart() {
		LocalDateTime initialDate = spendingRepository.findTopByOrderByDateAsc().getDate();
		LocalDateTime finalDate = spendingRepository.findTopByOrderByDateDesc().getDate();
		List<BigDecimal> spendings = spendingRepository.getTotalSpendingPerMonth(initialDate, finalDate, TypeSpending.GROUPING.getCode())
				.stream()
				.map(s -> s.setScale(2, RoundingMode.HALF_DOWN))
				.collect(Collectors.toList());
		List<BigDecimal> budgets = revenueRepository.getTotalRevenuePerMonth(initialDate, finalDate)
				.stream()
				.map(s -> s.subtract(GOAL_MONEY).setScale(2, RoundingMode.HALF_DOWN))
				.collect(Collectors.toList());
		List<BigDecimal> percentBudget = getPercentBudgetByMonth(spendings, budgets);
		List<String> dates = spendingService.getListDatesSpending();
		return new BudgetChart(spendings, budgets, percentBudget, dates);
	}
	
	private List<BigDecimal> getPercentBudgetByMonth(List<BigDecimal> spendings, List<BigDecimal> budgets) {
		List<BigDecimal> percentsBudgets = new ArrayList<>();
		int i = 0;
		for (BigDecimal spending : spendings) {
			if (i < budgets.size()) {
				BigDecimal budgetMoney = budgets.get(i++).setScale(2);
				spending = spending.multiply(new BigDecimal(100));
				BigDecimal percent = spending.divide(budgetMoney, 2, RoundingMode.HALF_DOWN);
				percentsBudgets.add(percent.setScale(2, RoundingMode.DOWN));
			}
		}
		return percentsBudgets;
	}
	
	public SpendingCategoryDTO getSpendingCategoryChart() {
		return new SpendingCategoryDTO(spendingRepository.getListSpendingCategory(TypeSpending.GROUPING.getCode()));
	}
	
	public SpendingCategoryDTO getSpendingCategoryChartPerDate(String initialDate, String finalDate, String budget) {
		LocalDateTime lInitialDate = LocalDateTime.parse(initialDate + " 00:00:00",DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		LocalDateTime lFinalDate = LocalDateTime.parse(finalDate + " 00:00:00",DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		lInitialDate = lInitialDate.withDayOfMonth(1);
		LocalDate lFinal = lFinalDate.toLocalDate(); 
		lFinalDate = lFinalDate.withDayOfMonth(lFinalDate.getMonth().length(lFinal.isLeapYear()));
		return new SpendingCategoryDTO(spendingRepository.getListSpendingCategoryPerDate(lInitialDate, lFinalDate, TypeSpending.GROUPING.getCode(), isBudget(budget)));
	}
	
	private Boolean isBudget(String budget) {
		if (budget == null) {
			return null;
		}
		switch (budget) {
			case "Only budget" : return true;
			case "No budget" : return false;
			default : return null;
		}
	}

	public ChartSpendingDayDTO getSpendingPerDay() {
		List<SpendingDayDTO> spendings = spendingRepository.getListSpendingPerDay();
		return getSpendingPerDay(spendings);
	}

	private ChartSpendingDayDTO getSpendingPerDay(List<SpendingDayDTO> spendings) {
		List<BigDecimal> values = new ArrayList<>();
		List<String> days = new ArrayList<>();
		int positionResults = 0;
		int lastDay = LocalDate.now().withDayOfMonth(LocalDate.now().getMonth().length(LocalDate.now().isLeapYear())).getDayOfMonth();
		LocalDateTime initialDate = LocalDateTime.now().withDayOfMonth(1);
		for(int day = 1; day <= lastDay; day++) {
			BigDecimal value = BigDecimal.ZERO;
			String stringDay = getDayOfWeek(initialDate) + day;
			if (spendings.size() > 0 && spendings.size() > positionResults && spendings.get(positionResults).getDate().getDayOfMonth() == day) {
				value = spendings.get(positionResults++).getValue();
			}
			values.add(value);
			days.add(stringDay);
			initialDate = initialDate.plusDays(1L);
		}
		return new ChartSpendingDayDTO(values, days);
	}

	private String getDayOfWeek(LocalDateTime date) {
		Date date2 = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date2);
	    return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, new Locale("PT-BR")) + "-";
	}

}
