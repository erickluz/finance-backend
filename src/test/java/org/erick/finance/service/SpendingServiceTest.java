package org.erick.finance.service;

import java.util.List;

import org.erick.finance.dto.DateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpendingServiceTest {
	@Autowired
	private SpendingService spendingService;
	
	@Test
	public void mustListDatesSpending() {
		List<DateDTO> dates = spendingService.getDatesSpending();
		System.out.println(dates);
	}
}
