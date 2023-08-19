package org.erick.finance.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartupService implements InitializingBean {

	@Autowired
	private SpendingCheckMonthService spendingCheckMonthService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		spendingCheckMonthService.verifyAndCreateMissingSpendingCheckMonth();
	}

}
