package org.erick.finance.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.erick.finance.domain.Card;
import org.erick.finance.domain.Spending;
import org.erick.finance.domain.SpendingCategory;
import org.erick.finance.domain.TypeSpending;
import org.erick.finance.dto.DateDTO;
import org.erick.finance.dto.SpendingDTO;
import org.erick.finance.repository.SpendingRepository;
import org.erick.finance.util.Month;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpendingService {
	@Autowired
	private SpendingRepository rep;
	@Autowired
	private SpendingCategoryService categoryService;
	@Autowired
	private UtilService utilService;
	@Autowired
	private CardService cardService;
	
	public List<Spending> listAll() {
		return rep.findAll();
	}
	
	public List<Spending> listByMonth(String date) {
		LocalDateTime datetime = LocalDateTime.parse(date+ " 00:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy[ HH:mm:ss]"));
		return rep.listByMonth(datetime.getMonthValue(), datetime.getYear(), TypeSpending.GROUPING.getCode());
	}
	
	public Spending save(Spending spending) {
		return rep.save(spending);
	}
	
	public Spending save(SpendingDTO spendingDTO) {
		SpendingCategory spendingCategory = categoryService.findById(Long.valueOf(spendingDTO.getIdCategory()));
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy[ HH:mm:ss]");
		LocalDateTime date = LocalDateTime.parse(spendingDTO.getDate() + " 00:00:00", dateTimeFormatter);
		BigDecimal value = BigDecimal.valueOf(Double.valueOf(spendingDTO.getValue()));
		Card card = getCardSpending(spendingDTO);
		Spending spending = new Spending(parseId(spendingDTO), spendingDTO.getName(), date, value, spendingCategory, getType(spendingDTO), 
				null, null, card);
		spending.setSpendingsInsallments(getParts(spendingDTO, spending, card));
		return rep.save(spending);
	}

	private Card getCardSpending(SpendingDTO spendingDTO) {
		Card card = null;
		if (spendingDTO.getIdCard() != null) {
			card = cardService.findById(Long.valueOf(spendingDTO.getIdCard()));
		}
		return card;
	}

	private Long parseId(SpendingDTO spendingDTO) {
		return (spendingDTO.getId() != null && !spendingDTO.getId().equals("")) ? Long.valueOf(spendingDTO.getId()) : null;
	}
	
	private List<Spending> getParts(SpendingDTO spendingDTO, Spending spending, Card card) {
		List<Spending> partsSpending = new ArrayList<>();
		if (spendingDTO.getParts() != null && !spendingDTO.getParts().equals("")) {
			createPartSpendings(spendingDTO, partsSpending, spending, card);
		}
		return partsSpending;
	}

	private void createPartSpendings(SpendingDTO spendingDTO, List<Spending> partsSpending, Spending spending, Card card) {
		Integer parts = Integer.valueOf(spendingDTO.getParts());
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy[ HH:mm:ss]");
		LocalDateTime date = LocalDateTime.parse(spendingDTO.getDate() + " 00:00:00", dateTimeFormatter);
		for (int i = 0; i < parts; i++) {
			String name = spendingDTO.getName() + " (" + (i+1) + "/" + parts + ")";				
			BigDecimal value = BigDecimal.valueOf(Double.valueOf(spendingDTO.getValue()));
			SpendingCategory spendingCategory = categoryService.findById(Long.valueOf(spendingDTO.getIdCategory()));
			Spending spendingPart = new Spending(null, name, date, value, spendingCategory, TypeSpending.PART.getCode(), spending, null, card);
			date = date.plusMonths(1L);
			partsSpending.add(spendingPart);
		}
	}

	private Short getType(SpendingDTO spendingDTO) {
		return (spendingDTO.getParts() == null || spendingDTO.getParts().equals("")) 
				? TypeSpending.NORMAL.getCode() : TypeSpending.GROUPING.getCode();
	}

	public void delete(Long id) {
		Spending spending = findById(id);
		if (spending.getType().equals(TypeSpending.GROUPING.getCode()) || spending.getType().equals(TypeSpending.PART.getCode())) {
			Spending spendingGroup = findById(spending.getSpendingGroup().getId());
			rep.deleteById(spendingGroup.getId());
		} else {
			rep.deleteById(id);
		}
	}
	
	public Spending findById(Long id) {
		return rep.findById(id).orElse(null);
	}
	
	public List<DateDTO> getDatesSpending() {
		LocalDateTime initialDate = rep.findTopByOrderByDateAsc().getDate();
		LocalDateTime finalDate = rep.findTopByOrderByDateDesc().getDate();;
		return utilService.getListDatesBetweenTwoDates(initialDate, finalDate);
	}
	
	public List<String> getListDatesSpending(LocalDateTime initialDate, LocalDateTime finalDate) {
		List<Integer> dates = rep.getListSpending(TypeSpending.GROUPING.getCode(), initialDate, finalDate);
		return dates.stream().map(date -> {
			return Month.fromNumber(date).getShortName();
		}).collect(Collectors.toList());
	}

	public void update(SpendingDTO spendingDTO) throws Exception {
		Spending spending = findById(parseId(spendingDTO));
		if (spending == null) {
			throw new Exception();
		}
		if (spending.getSpendingGroup() != null) {
			spending = findById(spending.getSpendingGroup().getId());
		}
		spending.setName(spendingDTO.getName());
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy[ HH:mm:ss]");
		LocalDateTime date = LocalDateTime.parse(spendingDTO.getDate() + " 00:00:00", dateTimeFormatter);
		spending.setDate(date);
		BigDecimal value = BigDecimal.valueOf(Double.valueOf(spendingDTO.getValue()));
		spending.setValue(value);
		SpendingCategory category = categoryService.findById(Long.valueOf(spendingDTO.getIdCategory()));
		spending.setCategory(category);
		spending.setCard(getCardSpending(spendingDTO));
		updateInstallments(spending, spendingDTO);
		rep.save(spending);
	}

	private void updateInstallments(Spending spending, SpendingDTO spendingDTO) {
		Integer installments = Integer.valueOf(spendingDTO.getParts());
		if (installments > 0) {
			spending.getSpendingsInsallments().clear();
			LocalDateTime date = spending.getDate();
			for (int i = 0; i < installments; i++) {
				String name = spending.getName() + " (" + (i+1) + "/" + installments + ")";				
				Spending spendingPart = new Spending(null, name, date, spending.getValue(), spending.getCategory(), TypeSpending.PART.getCode(), 
						spending, null, spending.getCard());
				date = date.plusMonths(1L);
				spending.getSpendingsInsallments().add(spendingPart);
			}
		}
	}

	public List<Spending> getPartsSpending(Long id) {
		return rep.getFirstPartSpending(id);
	}
	
	public List<SpendingDTO> listSpendingToDTO(String dateParam) {
		List<Spending> spendings = listByMonth(dateParam);
		List<SpendingDTO> spendingsDTO = spendings.stream()
				.map(spending -> {
					String date = spending.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
					String value = "R$ " + spending.getValue();
					String categoria = spending.getCategory().getName();
					String card = (spending.getCard() != null) ? spending.getCard().getIssuer() : "Sem Cartão";
					String idCard = (spending.getCard() != null) ? spending.getCard().getId().toString() : "";	
					String namePart = getPartName(spending);
					SpendingDTO spendingDTO = new SpendingDTO(spending.getId().toString(), spending.getName(), namePart, spending.getType(), 
							date, value, categoria, null, idCard, card, null);
					setParts(spendingDTO, spending);
					return spendingDTO;
				})
				.collect(Collectors.toList());
		return spendingsDTO;
	}
	
	public SpendingDTO getSpendingDTO(Spending spending) {
		String date = spending.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		String value = spending.getValue().toString();
		String categoria = spending.getCategory().getId().toString();
		String card = (spending.getCard() != null) ? spending.getCard().getId().toString() : null;
		String parts = getParts(spending);
		String name = spending.getName();
		String namePart = getPartName(spending);
		return new SpendingDTO(spending.getId().toString(), name, namePart, spending.getType(), date, value, categoria, parts, card, null, null);
	}
	
	private void setParts(SpendingDTO spendingDTO, Spending spending) {
		if (TypeSpending.fromCode(spending.getType()).equals(TypeSpending.PART)) {
			List<Spending> groupSpendings = getPartsSpending(spending.getSpendingGroup().getId());
			spendingDTO.setIsFirstPart(isFirstPart(spending, groupSpendings));
			spendingDTO.setParts(String.valueOf(groupSpendings.size()));
		}
	}

	private Boolean isFirstPart(Spending spending, List<Spending> groupSpendings) {
		if (groupSpendings != null && groupSpendings.size() > 0) {
			Spending firstPartSpending = groupSpendings.get(0);
			return firstPartSpending.getId().equals(spending.getId());
		} else {
			return false;
		}
	}

	private String getPartName(Spending spending) {
		String partName = null;
		if (spending.getSpendingGroup() != null) {
			partName = spending.getSpendingGroup().getName();
		}
		return partName;
	}

	private String getParts(Spending spending) {
		Integer parts = 0;
		if (spending.getSpendingGroup() != null) {
			List<Spending> spendings = getPartsSpending(spending.getSpendingGroup().getId());
			if (spendings != null) {
				parts = spendings.size();
			}
		}
		return parts.toString();
	}
}
