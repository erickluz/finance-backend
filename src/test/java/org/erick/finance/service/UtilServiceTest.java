package org.erick.finance.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.erick.finance.dto.DateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UtilServiceTest {

	@Autowired
	private UtilService utilService;
	
	@Test
	public void mustGetListOfDates() {
		LocalDateTime initialDate = LocalDateTime.parse("01/02/2023 00:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy[ HH:mm:ss]"));
		LocalDateTime finalDate = LocalDateTime.parse("01/03/2023 00:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy[ HH:mm:ss]"));
		List<DateDTO> dates = utilService.getListDatesBetweenTwoDates(initialDate, finalDate);
		System.out.println(dates);
	}
}
