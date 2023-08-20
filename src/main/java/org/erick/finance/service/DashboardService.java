package org.erick.finance.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.erick.finance.domain.Budget;
import org.erick.finance.domain.BudgetChart;
import org.erick.finance.domain.MonthStats;
import org.erick.finance.domain.TotalsStats;
import org.erick.finance.domain.TypeSpending;
import org.erick.finance.dto.ChartSpendingDayDTO;
import org.erick.finance.dto.RevenuePerMonthDTO;
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
	@Autowired
	private BudgetService budgetService;
	
	public MonthStats getMonthStats(String month) {
		LocalDateTime monthStatsDate =	 getMonthStatsDate(month);
		BigDecimal budgetValue = budgetService.getBudgetValueByDate(monthStatsDate);
		BigDecimal monthRevenue = revenueRepository.getTotalRevenueByMonth(monthStatsDate);
		BigDecimal monthSpending = spendingRepository.getTotalSpendingByMonth(monthStatsDate);
		BigDecimal monthBalance = monthRevenue.subtract(monthSpending);
		BigDecimal monthBudgetMoney = monthRevenue.subtract(budgetValue);
		BigDecimal monthBalanceGoal = monthBudgetMoney.subtract(monthSpending); 
		BigDecimal monthBudgetPercentage = monthSpending.multiply(new BigDecimal(100)).divide(monthBudgetMoney, 2, RoundingMode.HALF_DOWN);
		return new MonthStats(monthRevenue.toString(), monthSpending.toString(), monthBalance.toString(), monthBalanceGoal.toString(), monthBudgetMoney.toString(), budgetValue.toString(), monthBudgetPercentage.toString());
	}
	
	private LocalDateTime getMonthStatsDate(String month) {
		if (month != null) {
			return LocalDateTime.parse(month + " 00:00:00",DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).withDayOfMonth(1); 
		} else {
			return LocalDateTime.now();
		}
	}

	public TotalsStats getTotalsStats(String sInitialDate, String sFinalDate) {
		LocalDateTime initialDate = LocalDateTime.parse(sInitialDate + " 00:00:00",DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).withDayOfMonth(1);
		LocalDateTime finalDate = LocalDateTime.parse(sFinalDate + " 00:00:00",DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		int lastDay = finalDate.toLocalDate().withDayOfMonth(finalDate.toLocalDate().getMonth().length(finalDate.toLocalDate().isLeapYear())).getDayOfMonth();
		finalDate = finalDate.withDayOfMonth(lastDay);
		BigDecimal totalRevenue = revenueRepository.getTotalRevenue(initialDate, finalDate);
		BigDecimal totalSpeding = spendingRepository.getTotalSpending(initialDate, finalDate);
		BigDecimal totalBalance = totalRevenue.subtract(totalSpeding);
		BigDecimal totalBudgetValue = budgetService.getTotalBudgetValue(initialDate, finalDate);
		BigDecimal totalBudgetBalance = getTotalBudgetBalance(totalBudgetValue, initialDate, finalDate);
		BigDecimal totalDebt = spendingRepository.getTotalDebt(initialDate, finalDate);
		return new TotalsStats(totalRevenue.toString(), totalSpeding.toString(), totalBudgetValue.toString(), totalBudgetBalance.toString(), totalBalance.toString(), totalDebt.toString());
	}

	private BigDecimal getTotalBudgetBalance(BigDecimal totalBudgetValue, LocalDateTime initialDate, LocalDateTime finalDate) {
		BigDecimal totalSpending = spendingRepository.getTotalSpending(initialDate, finalDate);
		BigDecimal totalRevenue = revenueRepository.getTotalRevenue(initialDate, finalDate);
		totalRevenue = totalRevenue.subtract(totalBudgetValue);
		return totalRevenue.subtract(totalSpending);
	}
	
	public BudgetChart getBudgetChart(String sInitialDate, String sFinalDate) {
		LocalDateTime initialDate = getInitialDate(sInitialDate);
		LocalDateTime finalDate = getFinalDate(sFinalDate);
		List<BigDecimal> spendings = spendingRepository.getTotalSpendingPerMonth(initialDate, finalDate)
				.stream()
				.map(s -> s.setScale(2, RoundingMode.HALF_DOWN))
				.collect(Collectors.toList());
		List<BigDecimal> budgets = revenueRepository.getTotalRevenuePerMonth(initialDate, finalDate)
				.stream()
				.map(s -> {
					BigDecimal budgetValue = getBudgetByMonthAndYear(s);
					return s.getValue().subtract(budgetValue).setScale(2, RoundingMode.HALF_DOWN);
				}).collect(Collectors.toList());
		List<BigDecimal> percentBudget = getPercentBudgetByMonth(spendings, budgets);
		List<String> dates = spendingService.getListDatesSpending(initialDate, finalDate);
		return new BudgetChart(spendings, budgets, percentBudget, dates);
	}

	private BigDecimal getBudgetByMonthAndYear(RevenuePerMonthDTO s) {
		Budget budget = budgetService.getBudgetByDate(s.getDate());
		if (budget == null) {
			LocalDateTime previousMonth = s.getDate().minusMonths(1L);
			budget = budgetService.getBudgetByDate(previousMonth);
			Budget newBudget = new Budget(null, s.getDate(), budget.getValue());
			budget = budgetService.save(newBudget);
		}
		return budget.getValue();
	}

	private LocalDateTime getFinalDate(String sFinalDate) {
		LocalDate sixMonthsAhead = LocalDate.now().withDayOfMonth(1).plus(6, ChronoUnit.MONTHS); 
		int lastDay = sixMonthsAhead.withDayOfMonth(sixMonthsAhead.getMonth().length(sixMonthsAhead.isLeapYear())).getDayOfMonth();
		if (sFinalDate != null) {
			return LocalDateTime.parse(sFinalDate + " 00:00:00",DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).withDayOfMonth(lastDay);
		} else {			
			return LocalDateTime.now().withDayOfMonth(1).plus(6, ChronoUnit.MONTHS).withDayOfMonth(lastDay);
		}
	}

	private LocalDateTime getInitialDate(String sInitialDate) {
		if (sInitialDate != null) {
			return LocalDateTime.parse(sInitialDate + " 00:00:00",DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).withDayOfMonth(1);
		} else {
			return LocalDateTime.now().withDayOfMonth(1).minus(5, ChronoUnit.MONTHS);
		}
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
		return new SpendingCategoryDTO(spendingRepository.getListSpendingCategory());
	}
	
	public SpendingCategoryDTO getSpendingCategoryChartPerDate(String initialDate, String finalDate, String budget) {
		LocalDateTime lInitialDate = LocalDateTime.parse(initialDate + " 00:00:00",DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		LocalDateTime lFinalDate = LocalDateTime.parse(finalDate + " 00:00:00",DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		lInitialDate = lInitialDate.withDayOfMonth(1);
		LocalDate lFinal = lFinalDate.toLocalDate(); 
		lFinalDate = lFinalDate.withDayOfMonth(lFinalDate.getMonth().length(lFinal.isLeapYear()));
		SpendingCategoryDTO spendingCategory = new SpendingCategoryDTO(spendingRepository.getListSpendingCategoryPerDate(lInitialDate, lFinalDate, TypeSpending.GROUPING_PART.getCode(), isBudget(budget)));
		BigDecimal total = spendingCategory.getItens().stream().map(i -> BigDecimal.valueOf(i.getValue())).reduce(BigDecimal.ZERO, BigDecimal::add);
		spendingCategory.getItens()
		.stream()
		.forEach(i -> 
			i.setPercent(( (BigDecimal.valueOf(i.getValue()).multiply(BigDecimal.valueOf(100))).divide(total, RoundingMode.HALF_DOWN).toString() + "%"))
		);
		return spendingCategory;
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
