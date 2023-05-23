package org.erick.finance.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.erick.finance.domain.Budget;
import org.erick.finance.dto.BudgetDTO;
import org.erick.finance.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BudgetService {
	@Autowired
	private BudgetRepository budgetRepository;
	
	public BigDecimal getBudgetByDate(LocalDateTime date) {
		return budgetRepository.getBudgetByDate(date);
	}

	public BigDecimal getTotalBudgetValue() {
		return budgetRepository.getTotalBudgetValue();
	}
	
	public Budget save(BudgetDTO budget) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy[ HH:mm:ss]");
		LocalDateTime date = LocalDateTime.parse(budget.getDate() + " 00:00:00", dateTimeFormatter);
		Budget b = new Budget(budget.getId(), date, budget.getValue());
		return budgetRepository.save(b);
	}
	
	public List<Budget> findAll() {
		return budgetRepository.findAll(sortByDateAsc() );
	}
	
	public Budget findById(Long id) {
		return budgetRepository.findById(id).orElse(null);
	}
	
	public void delete(Long id) {
		budgetRepository.deleteById(id);
	}
	
    private Sort sortByDateAsc() {
        return Sort.by(Sort.Direction.ASC, "date");
    }
}
