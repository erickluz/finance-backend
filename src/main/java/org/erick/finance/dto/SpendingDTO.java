package org.erick.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SpendingDTO {
	private Long id;
	private String name;
	private String date;
	private String value;
	private String idCategory;
}
