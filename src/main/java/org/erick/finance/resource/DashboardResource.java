package org.erick.finance.resource;

import org.erick.finance.domain.BudgetChart;
import org.erick.finance.domain.MonthStats;
import org.erick.finance.domain.TotalsStats;
import org.erick.finance.dto.ChartSpendingDayDTO;
import org.erick.finance.dto.SpendingCategoryDTO;
import org.erick.finance.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/dashboard")
@RestController
public class DashboardResource {

	@Autowired
	private DashboardService dashboardService;
	
	@CrossOrigin
	@GetMapping("/totalsStats")
	public ResponseEntity<TotalsStats> getTotalStats(String initialDate, String finalDate) {
		return ResponseEntity.ok(dashboardService.getTotalsStats(initialDate, finalDate));
	}
	
	@CrossOrigin
	@GetMapping("/monthStats")
	public ResponseEntity<MonthStats> getMonthStats(@RequestParam(required = false) String date) {
		return ResponseEntity.ok(dashboardService.getMonthStats(date));
	}
	
	@CrossOrigin
	@GetMapping("/budgetChart")
	public ResponseEntity<BudgetChart> getBudgetsChart(
			@RequestParam(required = false) String initialDate, 
			@RequestParam(required = false) String finalDate) {
		return ResponseEntity.ok(dashboardService.getBudgetChart(initialDate, finalDate));
	}
	
	@CrossOrigin
	@GetMapping("/spendingchart")
	public ResponseEntity<SpendingCategoryDTO> getSpendingCategoryChart(
			@RequestParam(required = false) String initialDate, 
			@RequestParam(required = false) String finalDate,
			@RequestParam(required = false) String budget) {
		if (initialDate == null || initialDate.equals("") || finalDate == null || finalDate.equals("")) {
			return ResponseEntity.ok(dashboardService.getSpendingCategoryChart()); 
		} else {
			return ResponseEntity.ok(dashboardService.getSpendingCategoryChartPerDate(initialDate, finalDate, budget));
		}
	}
	
	@CrossOrigin
	@GetMapping("/spendingByDay")
	public ResponseEntity<ChartSpendingDayDTO> getSpendingPerDay() {
		return ResponseEntity.ok(dashboardService.getSpendingPerDay());
	}

}
