package org.erick.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCheckSpendingDTO {
	private String id;
	private String name;
	private String date;
	private String card;
	private String value;
	private Boolean isAssociable;
	private Boolean checked;
	private Boolean isJustified;
}
