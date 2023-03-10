package org.erick.finance.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	
	public List<Spending> listAll() {
		return rep.findAll();
	}
	
	public List<Spending> listByMonth(String date) {
		LocalDateTime datetime = LocalDateTime.parse(date+ " 00:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy[ HH:mm:ss]"));
		return rep.listByMonth(datetime.getMonthValue(), TypeSpending.GROUPING.getCode());
	}
	
	public Spending save(Spending spending) {
		return rep.save(spending);
	}
	
	public Spending save(SpendingDTO spendingDTO) {
		System.out.println(spendingDTO.toString());
		SpendingCategory spendingCategory = categoryService.findById(Long.valueOf(spendingDTO.getIdCategory()));
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy[ HH:mm:ss]");
		LocalDateTime date = LocalDateTime.parse(spendingDTO.getDate() + " 00:00:00", dateTimeFormatter);
		BigDecimal value = BigDecimal.valueOf(Double.valueOf(spendingDTO.getValue()));		
		Spending spending = new Spending(spendingDTO.getId(), spendingDTO.getName(), date, value, spendingCategory, getType(spendingDTO), 
				null, null);
		spending.setSpendingsInsallments(getParts(spendingDTO, spending));
		return rep.save(spending);
	}
	
	private List<Spending> getParts(SpendingDTO spendingDTO, Spending spending) {
		List<Spending> partsSpending = new ArrayList<>();
		if (spendingDTO.getParts() != null && !spendingDTO.getParts().equals("")) {
			createPartSpendings(spendingDTO, partsSpending, spending);
		}
		return partsSpending;
	}

	private void createPartSpendings(SpendingDTO spendingDTO, List<Spending> partsSpending, Spending spending) {
		Integer parts = Integer.valueOf(spendingDTO.getParts());
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy[ HH:mm:ss]");
		LocalDateTime date = LocalDateTime.parse(spendingDTO.getDate() + " 00:00:00", dateTimeFormatter);
		for (int i = 0; i < parts; i++) {
			String name = spendingDTO.getName() + " (" + (i+1) + "/" + parts + ")";				
			BigDecimal value = BigDecimal.valueOf(Double.valueOf(spendingDTO.getValue()));
			SpendingCategory spendingCategory = categoryService.findById(Long.valueOf(spendingDTO.getIdCategory()));
			Spending spendingPart = new Spending(null, name, date, value, spendingCategory, TypeSpending.PART.getCode(), spending, null);
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
	
	public List<String> getListDatesSpending() {
		List<Integer> dates = rep.getListSpending(TypeSpending.GROUPING.getCode());
		return dates.stream().map(date -> {
			return Month.fromNumber(date).getShortName();
		}).collect(Collectors.toList());
	}

	public void update(SpendingDTO spendingDTO) throws Exception {
		Spending spending = findById(spendingDTO.getId());
		if (spending == null) {
			throw new Exception();
		}
		spending.setName(spendingDTO.getName());
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy[ HH:mm:ss]");
		LocalDateTime date = LocalDateTime.parse(spendingDTO.getDate() + " 00:00:00", dateTimeFormatter);
		spending.setDate(date);
		BigDecimal value = BigDecimal.valueOf(Double.valueOf(spendingDTO.getValue()));
		spending.setValue(value);
		SpendingCategory category = categoryService.findById(Long.valueOf(spendingDTO.getIdCategory()));
		spending.setCategory(category);
		rep.save(spending);
	}

}
