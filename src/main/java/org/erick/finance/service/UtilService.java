package org.erick.finance.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.erick.finance.dto.DateDTO;
import org.erick.finance.util.Month;
import org.springframework.stereotype.Service;

@Service
public class UtilService {

	public List<DateDTO> getListDatesBetweenTwoDates(LocalDateTime initialDate, LocalDateTime finalDate) {
		List<DateDTO> dates = new ArrayList<>();
		initialDate = initialDate.withDayOfMonth(1);
		finalDate = finalDate.withDayOfMonth(1);
		LocalDateTime actualDate = initialDate;
		while (actualDate.isBefore(finalDate) || actualDate.isEqual(finalDate)) {
			Month actualMonth = Month.fromNumber(actualDate.getMonthValue());
			String monthNumber = actualMonth.getMonthNumber().toString();
			String shortDate = actualMonth.getShortName() + "/" + actualDate.getYear();
			String stringDate = actualDate.withDayOfMonth(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			dates.add(new DateDTO(monthNumber, shortDate, stringDate));
			actualDate = actualDate.plusMonths(1L);
		}
		return dates;
	}
	
}
