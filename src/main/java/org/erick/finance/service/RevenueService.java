package org.erick.finance.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.erick.finance.domain.Revenue;
import org.erick.finance.domain.TypeRevenue;
import org.erick.finance.dto.RevenueDTO;
import org.erick.finance.repository.RevenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.ToString;

@Service
@ToString
public class RevenueService {

	@Autowired
	private RevenueRepository rep;
	@Autowired
	private TypeRevenueService typeRevenueService;
	
	public List<Revenue> listAll() {
		return rep.findAll();
	}
	
	public Revenue save(Revenue revenue) {
		return rep.save(revenue);
	}
	
	public Revenue save(RevenueDTO revenueDTO) {
		TypeRevenue typeRevenue = typeRevenueService.findById(Long.valueOf(revenueDTO.getIdType()));
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy[ HH:mm:ss]");
		LocalDateTime date = LocalDateTime.parse(revenueDTO.getDate() + " 00:00:00", dateTimeFormatter);
		BigDecimal value = BigDecimal.valueOf(Double.valueOf(revenueDTO.getValue()));
		Revenue revenue = new Revenue(revenueDTO.getId(), date, typeRevenue, value);
		return rep.save(revenue);
	}
	
	public void delete(Long id) {
		rep.deleteById(id);
	}

	public Revenue findById(Long id) {
		return rep.findById(id).orElse(null);
	}
	
}
