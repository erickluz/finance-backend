package org.erick.finance.domain;

import java.util.List;

import org.erick.finance.dto.CreditCardFileDTO;

public interface CreditCardBillFile {
	List<CreditCardFileDTO> parse() throws Exception;
}
