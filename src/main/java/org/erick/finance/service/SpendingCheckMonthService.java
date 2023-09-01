package org.erick.finance.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.erick.finance.domain.CreditCardSpending;
import org.erick.finance.domain.CreditCardSpendingType;
import org.erick.finance.domain.Spending;
import org.erick.finance.domain.SpendingCheck;
import org.erick.finance.domain.SpendingCheckMonth;
import org.erick.finance.domain.SpendingCreditCardSpending;
import org.erick.finance.domain.TypeSpending;
import org.erick.finance.dto.AssociationIdDTO;
import org.erick.finance.dto.AssociationsIDSDTO;
import org.erick.finance.dto.ItemCheckSpendingDTO;
import org.erick.finance.dto.SpendingCheckAssociationDTO;
import org.erick.finance.dto.SpendingCheckDTO;
import org.erick.finance.dto.SpendingCheckMonthDTO;
import org.erick.finance.repository.SpendingCheckMonthRepository;
import org.erick.finance.repository.SpendingCheckRepository;
import org.erick.finance.repository.SpendingCreditCardSpendingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Autowired
	private SpendingCreditCardSpendingRepository spendingCreditCardSpendingRepository;
	@Autowired
	private CreditCardSpendingService creditCardSpendingService;
	
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
	
	public List<SpendingCheckAssociationDTO> getSpendingsCheckAssociation(String date, String association, String associable) {
		LocalDateTime spendingDate = LocalDateTime.parse(date + " 00:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy[ HH:mm:ss]"));
		LocalDateTime creditCardSpendingDate = spendingDate.plusMonths(1L);
		List<SpendingCheckAssociationDTO> spendingsCheckAssociation = rep.getSpendingsCheckAssociation(spendingDate, association, associable);
		getCreditCardSpendingsWithoutAssociation(creditCardSpendingDate, spendingsCheckAssociation, association);
		return spendingsCheckAssociation;
	}

	private void getCreditCardSpendingsWithoutAssociation(LocalDateTime creditCardSpendingDate,
			List<SpendingCheckAssociationDTO> spendingsCheckAssociation, String association) {
		List<CreditCardSpending> creditCardSpendings = creditCardbillService.getCreditCardSpendingByDueDateBill(creditCardSpendingDate, association);
		creditCardSpendings.forEach(ccs -> {
			ItemCheckSpendingDTO spending = new ItemCheckSpendingDTO();
			ItemCheckSpendingDTO creditCardSpending = new ItemCheckSpendingDTO(ccs.getId().toString(), ccs.getDescription(), ccs.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
					, ccs.getCreditCardBill().getCard().getIssuer(), ccs.getValue().toString(), ccs.getCreditCardBill().getCard().getIsChkByFile(), false, ccs.getIsJustified());
			SpendingCheckAssociationDTO spendingCheckAssociationDTO = new SpendingCheckAssociationDTO(spending, creditCardSpending);
			spendingsCheckAssociation.add(spendingCheckAssociationDTO);
			verifyJustifiedCreditCard(spendingCheckAssociationDTO, ccs);
		});
	}

	private void verifyJustifiedCreditCard(SpendingCheckAssociationDTO spendingCheckAssociationDTO, CreditCardSpending creditCardSpending) {
		if (creditCardSpending.getIsJustified() != null && creditCardSpending.getIsJustified()) {
			spendingCheckAssociationDTO.getSpending().setName("Justification: " + creditCardSpending.getJustification());
		}
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

	@Transactional
	public void associate(AssociationsIDSDTO associationsIdsDTO) {
		validateAssociateRegisters(associationsIdsDTO);
		Spending spendingToAssociate = getSpendingToAssociate(associationsIdsDTO);
		CreditCardSpending creditCardSpendingToAssociate = getCreditCardSpendingToAssociate(associationsIdsDTO);
		SpendingCheckMonth spendingCheckMonth = getSpendingCheckMonth(associationsIdsDTO);
		SpendingCreditCardSpending spendingCreditCardSpending = new SpendingCreditCardSpending(null, creditCardSpendingToAssociate, spendingToAssociate, spendingCheckMonth);
		spendingCreditCardSpendingRepository.save(spendingCreditCardSpending);
	}

	private SpendingCheckMonth getSpendingCheckMonth(AssociationsIDSDTO associationsIdsDTO) {
		return findById(associationsIdsDTO.getIdSpendingCheckMonth());
	}

	private CreditCardSpending getCreditCardSpendingToAssociate(AssociationsIDSDTO associationsIdsDTO) {
		if (associationsIdsDTO.getCreditCardIds().size() > 1) {
			Short type = CreditCardSpendingType.GROUPING.getCode();
			CreditCardSpending creditCardSpendingGrouping = new CreditCardSpending(null, null, null, LocalDateTime.now(), null, type, false, null, null, new ArrayList<>(), null, null);
			BigDecimal totalValue = BigDecimal.ZERO;
			CreditCardSpending creditCardSpendingChildren = null;
			for (Long ccs : associationsIdsDTO.getCreditCardIds()) {
				creditCardSpendingChildren = creditCardSpendingService.findById(ccs);
				creditCardSpendingGrouping.getCreditCardSpendingsChildren().add(creditCardSpendingChildren);
				creditCardSpendingGrouping.setCreditCardBill(creditCardSpendingChildren.getCreditCardBill());
				creditCardSpendingChildren.setCreditCardSpendingGrouping(creditCardSpendingGrouping);
				totalValue = totalValue.add(creditCardSpendingChildren.getValue());
			}
			creditCardSpendingGrouping.setValue(totalValue);
			creditCardSpendingGrouping = creditCardSpendingService.save(creditCardSpendingGrouping);
			return creditCardSpendingGrouping;
		} else {
			return creditCardSpendingService.findById(associationsIdsDTO.getCreditCardIds().get(0));
		}
	}

	private Spending getSpendingToAssociate(AssociationsIDSDTO associationsIdsDTO) {
		if (associationsIdsDTO.getSpendingsIds().size() > 1) {
			Short type = TypeSpending.GROUP_ASSOCIATION.getCode();
			Spending spendingGrouping = new Spending(null, null, LocalDateTime.now(), null, null, type, null, null, null, new ArrayList<>(), null, null, null);
			BigDecimal totalValue = BigDecimal.ZERO;
			for (Long s : associationsIdsDTO.getSpendingsIds()) {
				Spending spendingChildren = spendingService.findById(s);
				spendingGrouping.getSpendingsChildrens().add(spendingChildren);
				spendingGrouping.setDate(spendingChildren.getDate());
				spendingChildren.setSpendingGroupAssociation(spendingGrouping);
				totalValue = totalValue.add(spendingChildren.getValue());
			}	
			spendingGrouping.setValue(totalValue);
			spendingGrouping = spendingService.save(spendingGrouping);
			return spendingGrouping;
		} else {
			return spendingService.findById(associationsIdsDTO.getSpendingsIds().get(0));
		}
	}

	private void validateAssociateRegisters(AssociationsIDSDTO associationsIdsDTO) {
		// TODO Auto-generated method stub
	}

	public void desassociate(AssociationsIDSDTO associationsIdsDTO) {
		validateDesassociateRegisters(associationsIdsDTO);
		Long idSpending = associationsIdsDTO.getSpendingsIds().get(0);
		Spending spending = spendingService.findById(idSpending);
		Long idCreditCardSpending = associationsIdsDTO.getCreditCardIds().get(0);
		CreditCardSpending creditCardSpending = creditCardSpendingService.findById(idCreditCardSpending);
		SpendingCreditCardSpending spendingCreditCardSpending = rep.findByIdSpendingAndIdCreditCardSpending(idSpending, idCreditCardSpending);
		spendingCreditCardSpendingRepository.deleteById(spendingCreditCardSpending.getId());
		if (spending.getType().equals(TypeSpending.GROUP_ASSOCIATION.getCode())) {
			spending.getSpendingsChildrens().forEach(s -> {
				s.setSpendingGroupAssociation(null);
				s = spendingService.save(s);
			});
			spendingService.delete(spending.getId());			
		}
		if (creditCardSpending.getType().equals(CreditCardSpendingType.GROUPING.getCode())) {
			creditCardSpending.getCreditCardSpendingsChildren().forEach(ccs -> {
				ccs.setCreditCardSpendingGrouping(null);
				ccs = creditCardSpendingService.save(ccs);
			});
			creditCardSpendingService.delete(creditCardSpending.getId());						
		}
		
	}
	
	private void validateDesassociateRegisters(AssociationsIDSDTO associationsIdsDTO) {
		// TODO Auto-generated method stub
		
	}
	
	public void autoAssociate(LocalDateTime date) {
		List<AssociationIdDTO> associations = rep.autoAssociations(date, date.plusMonths(1L));
		SpendingCheckMonth spendingCheckMonth = rep.findByDate(date);
		associations.forEach(a -> {
			Spending spending = spendingService.findById(a.getIdSpending());
			CreditCardSpending creditCardSpending = creditCardSpendingService.findById(a.getIdCreditCardSpending());
			if (spendingCreditCardSpendingRepository.findByIdSendingAndIdCreditCardSpending(spending.getId(), creditCardSpending.getId()) == null) {
				SpendingCreditCardSpending spendingCreditCardSpending = new SpendingCreditCardSpending(null, creditCardSpending, spending, spendingCheckMonth);
				spendingCreditCardSpendingRepository.save(spendingCreditCardSpending);
			}
		});
	}

	public void justify(AssociationsIDSDTO associationsIdsDTO, String justification) {
		validateJustification(associationsIdsDTO);
		CreditCardSpending creditCardSpending = creditCardSpendingService.findById(associationsIdsDTO.getCreditCardIds().get(0));
		if (creditCardSpending.getIsJustified() == null || !creditCardSpending.getIsJustified()) {
			creditCardSpending.setIsJustified(true);
			creditCardSpending.setJustification(justification);
		} else {
			creditCardSpending.setIsJustified(false);
			creditCardSpending.setJustification(null);
		}
		creditCardSpendingService.save(creditCardSpending);
	}

	private void validateJustification(AssociationsIDSDTO associationsIdsDTO) {
		// TODO Auto-generated method stub
		
	}
	
	
}
