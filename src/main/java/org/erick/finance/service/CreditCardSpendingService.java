package org.erick.finance.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.erick.finance.domain.CreditCardSpending;
import org.erick.finance.repository.CreditCardSpendingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardSpendingService {

	@Autowired
	private CreditCardSpendingRepository rep;
	
	public List<CreditCardSpending> listAll() {
		return rep.findAll();
	}
	
	public CreditCardSpending save(CreditCardSpending creditCardSpending) {
		return rep.save(creditCardSpending);
	}
	
	public void delete(Long id) {
		rep.deleteById(id);
	}

	public CreditCardSpending findById(Long id) {
		return rep.findById(id).orElse(null);
	}

	public CreditCardSpending findByDescriptionAndDateAndPartAndCreditCardBill(String description, LocalDateTime date, String part,
			Long id, BigDecimal value) {
		return rep.findByDescriptionAndDateAndPartAndCreditCardBill(description, date, part, id, value);
	}
	
}
