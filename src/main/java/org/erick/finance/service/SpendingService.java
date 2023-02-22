package org.erick.finance.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.erick.finance.domain.Spending;
import org.erick.finance.domain.SpendingCategory;
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
		return rep.listByMonth(datetime.getMonthValue());
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
		Spending spending = new Spending(spendingDTO.getId(), spendingDTO.getName(), date, value, spendingCategory);
		return rep.save(spending);
	}
	
	public void delete(Long id) {
		rep.deleteById(id);
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
		List<Integer> dates = rep.getListSpending();
		return dates.stream().map(date -> {
			return Month.fromNumber(date).getShortName();
		}).collect(Collectors.toList());
	}
	
	
}
