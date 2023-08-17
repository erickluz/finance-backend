package org.erick.finance.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.erick.finance.domain.Card;
import org.erick.finance.domain.CreditCardBill;
import org.erick.finance.domain.CreditCardBillFile;
import org.erick.finance.domain.CreditCardBillXpInvestimentosFileCsv;
import org.erick.finance.domain.CreditCardSpending;
import org.erick.finance.dto.CreditCardFileDTO;
import org.erick.finance.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CardService {
	@Autowired
	private CardRepository rep;
	@Autowired
	private CreditCardSpendingService creditCardSpendingService;
	@Autowired
	private CreditCardBillService creditCardBillService;

	public List<Card> listAll() {
		return rep.findAll();
	}

	public Card save(Card card) {
		return rep.save(card);
	}

	public void delete(Long id) {
		rep.deleteById(id);
	}

	public Card findById(Long id) {
		return rep.findById(id).orElse(null);
	}

	public void importCreditCardBill(MultipartFile file, String idCreditBill) {
		CreditCardBillFile creditCardBillFile = new CreditCardBillXpInvestimentosFileCsv(file);
		List<CreditCardFileDTO> fileParsed = parseFile(creditCardBillFile);
		importRecords(fileParsed, idCreditBill);
	}

	private void importRecords(List<CreditCardFileDTO> fileParsed, String idCreditBill) {
		CreditCardBill creditCardBill = creditCardBillService.findById(Long.valueOf(idCreditBill));
		fileParsed.forEach(register -> {
			CreditCardSpending creditCardSpending = register.toCreditCardSpending();
			if (!isRegisterIsAlreadyExist(creditCardSpending, creditCardBill)) { //Corrigir para verificar duplicidade no arquivo
				creditCardSpending.setCreditCardBill(creditCardBill);
				creditCardSpendingService.save(creditCardSpending);
			}
		});
	}

	private boolean isRegisterIsAlreadyExist(CreditCardSpending register, CreditCardBill creditCardBill) {
		CreditCardSpending creditCardSpending = creditCardSpendingService.findByDescriptionAndDateAndPartAndCreditCardBill(
				register.getDescription(), register.getDate(), register.getPart(), creditCardBill.getId(), register.getValue());
		return (creditCardSpending != null);
	}

	private List<CreditCardFileDTO> parseFile(CreditCardBillFile creditCardBillFile) {
		List<CreditCardFileDTO> fileParsed = new ArrayList<>();
		try {
			fileParsed = creditCardBillFile.parse();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return fileParsed;
	}
}
