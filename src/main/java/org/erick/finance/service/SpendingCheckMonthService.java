package org.erick.finance.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.erick.finance.domain.CreditCardSpending;
import org.erick.finance.domain.Spending;
import org.erick.finance.domain.SpendingCheck;
import org.erick.finance.domain.SpendingCheckMonth;
import org.erick.finance.dto.ItemCheckSpendingDTO;
import org.erick.finance.dto.SpendingCheckAssociationDTO;
import org.erick.finance.dto.SpendingCheckDTO;
import org.erick.finance.dto.SpendingCheckMonthDTO;
import org.erick.finance.repository.SpendingCheckMonthRepository;
import org.erick.finance.repository.SpendingCheckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpendingCheckMonthService {

	@Autowired
	private CreditCardBillService creditCardbillService;
	@Autowired
	private SpendingService spendingService;
	@Autowired
	private SpendingCheckMonthRepository rep;
	@Autowired
	private SpendingCheckRepository spendingCheckRep;
	
	public List<SpendingCheckMonth> listAll() {
		return rep.findAll();
	}
	
	public SpendingCheckMonth save(SpendingCheckMonth spendingCheckMonth) {
		return rep.save(spendingCheckMonth);
	}

	private List<Date> getMissingMonthsSpendingCheckMonth() {
		return rep.getMissingMonthsSpendingCheckMonth();
	}
	
	public void delete(Long id) {
		rep.deleteById(id);
	}

	public SpendingCheckMonth findById(Long id) {
		return rep.findById(id).orElse(null);
	}
	
	public void verifyAndCreateMissingSpendingCheckMonth() {
		System.out.println("Checking missing months spending checks...");
		List<Date> spendingsCheck = getMissingMonthsSpendingCheckMonth();
		spendingsCheck.forEach(month -> {
			LocalDateTime date = Instant.ofEpochMilli(month.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
			System.out.println("Creating month " + date.getMonth().name());
			save(new SpendingCheckMonth(null, date, false));
		}
		);
	}

	public List<SpendingCheckMonthDTO> listSpendingCheckMonthToDTO() {
		List<SpendingCheckMonth> spendingsCheckMonth = listAll();
		return spendingsCheckMonth.stream().map(s -> toSpendingCheckMonthDTO(s)).collect(Collectors.toList());
	}
	
	public SpendingCheckMonthDTO toSpendingCheckMonthDTO(SpendingCheckMonth spendingCheckMonth) {
		String nameMonth = spendingCheckMonth.getDate().getMonth().name() + " - " + spendingCheckMonth.getDate().getYear();
		String date = spendingCheckMonth.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		return new SpendingCheckMonthDTO(spendingCheckMonth.getId().toString(), nameMonth, date, spendingCheckMonth.getIsChecked());
	}
	
	public List<SpendingCheckAssociationDTO> getSpendingsCheckAssociation(String date) {
		LocalDateTime spendingDate = LocalDateTime.parse(date + " 00:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy[ HH:mm:ss]"));
		LocalDateTime creditCardSpendingDate = spendingDate.plusMonths(1L);
		List<SpendingCheckAssociationDTO> spendingsCheckAssociation = rep.getSpendingsCheckAssociation(spendingDate);
		getCreditCardSpendingsWithoutAssociation(creditCardSpendingDate, spendingsCheckAssociation);
		return spendingsCheckAssociation;
	}

	private void getCreditCardSpendingsWithoutAssociation(LocalDateTime creditCardSpendingDate,
			List<SpendingCheckAssociationDTO> spendingsCheckAssociation) {
		List<CreditCardSpending> creditCardSpendings = creditCardbillService.getCreditCardSpendingByDueDateBill(creditCardSpendingDate);
		creditCardSpendings.forEach(ccs -> {
			ItemCheckSpendingDTO spending = new ItemCheckSpendingDTO();
//			ItemCheckSpendingDTO creditCardSpending = new ItemCheckSpendingDTO();
			ItemCheckSpendingDTO creditCardSpending = new ItemCheckSpendingDTO(ccs.getId().toString(), ccs.getDescription(), ccs.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
					, ccs.getCreditCardBill().getCard().getIssuer(), ccs.getValue().toString(), ccs.getCreditCardBill().getCard().getIsChkByFile(), false);
			SpendingCheckAssociationDTO spendingCheckAssociationDTO = new SpendingCheckAssociationDTO(spending, creditCardSpending);
			spendingsCheckAssociation.add(spendingCheckAssociationDTO);
		});
	}

	public void checkSpending(SpendingCheckDTO spendingCheckDTO) {
		Spending spending = spendingService.findById(spendingCheckDTO.getIdSpending());
		SpendingCheckMonth spendingoCheckMonth = findById(spendingCheckDTO.getIdSpendingCheckMonth());
		SpendingCheck spendingCheck = new SpendingCheck(null, spending, spendingoCheckMonth, spendingCheckDTO.getNote());
		spendingCheckRep.save(spendingCheck);
	}

	public void removeCheckSpending(Long idSpending) {
		SpendingCheck spendingCheck = spendingCheckRep.getSpendingCheckBySpendingId(idSpending);
		spendingCheckRep.deleteById(spendingCheck.getId());
	}
}
