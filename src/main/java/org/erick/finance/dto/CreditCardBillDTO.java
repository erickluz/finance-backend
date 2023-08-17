package org.erick.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardBillDTO {
	private String id;
	private String dueDate;
	private String uploadFileDate;
	private String idCard;
}
