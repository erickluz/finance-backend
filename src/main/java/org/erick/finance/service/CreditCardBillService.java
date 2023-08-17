package org.erick.finance.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.erick.finance.domain.Card;
import org.erick.finance.domain.CreditCardBill;
import org.erick.finance.dto.CreditCardBillDTO;
import org.erick.finance.repository.CardRepository;
import org.erick.finance.repository.CreditCardBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardBillService {

	@Autowired
	private CreditCardBillRepository rep;
	@Autowired
	private CardRepository cardRepository;
	
	public List<CreditCardBill> listAll() {
		return rep.findAll();
	}
	
	public CreditCardBill save(CreditCardBill creditCardBill) {
		return rep.save(creditCardBill);
	}
	
	public CreditCardBill save(CreditCardBillDTO creditCardBillDTO) {	
		CreditCardBill creditCardBill = findByDueDateAndCreditCard(creditCardBillDTO.getDueDate(), creditCardBillDTO.getIdCard());
		if (creditCardBill != null) {
			return creditCardBill;
		}
		Card card = cardRepository.findById(Long.valueOf(creditCardBillDTO.getIdCard())).orElse(null);
		LocalDateTime date = LocalDateTime.parse(creditCardBillDTO.getDueDate() + " 00:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy[ HH:mm:ss]"));
		creditCardBill = new CreditCardBill(null, date, LocalDateTime.now(), card);
		return save(creditCardBill);
	}
	
	private CreditCardBill findByDueDateAndCreditCard(String dueDate, String idCard) {
		LocalDateTime date = LocalDateTime.parse(dueDate + " 00:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy[ HH:mm:ss]"));
		Long idCardl = Long.valueOf(idCard);
		return rep.findByDueDateAndCreditCard(date, idCardl);
	}

	public void delete(Long id) {
		rep.deleteById(id);
	}

	public CreditCardBill findById(Long id) {
		return rep.findById(id).orElse(null);
	}
	
}
