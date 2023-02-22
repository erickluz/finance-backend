package org.erick.finance.resource;

import org.erick.finance.domain.BudgetChart;
import org.erick.finance.domain.Stats;
import org.erick.finance.dto.SpendingCategoryDTO;
import org.erick.finance.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/dashboard")
@RestController
public class DashboardResource {

	@Autowired
	private DashboardService dashboardService;
	
	@CrossOrigin
	@GetMapping
	public ResponseEntity<Stats> getStats() {
		return ResponseEntity.ok(dashboardService.getStats());
	}
	
	@CrossOrigin
	@GetMapping("/budget")
	public ResponseEntity<BudgetChart> getBudgetsChart() {
		return ResponseEntity.ok(dashboardService.getBudgetChart());
	}
	
	@CrossOrigin
	@GetMapping("/spendingchart")
	public ResponseEntity<SpendingCategoryDTO> getSpendingCategoryChart() {
		return ResponseEntity.ok(dashboardService.getSpendingCategoryChart());
	}

}
