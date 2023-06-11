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
	private String id;
	private String name;
	private String namePart;
	private Short type;
	private String date;
	private String value;
	private String idCategory;
	private String parts;
	private String idCard;
	private String card;
	private Boolean isFirstPart;
}
