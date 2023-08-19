package org.erick.finance.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AssociationsIDSDTO {
	private Long idSpendingCheckMonth;
	private List<Long> spendingsIds = new ArrayList<>();
	private List<Long> creditCardIds = new ArrayList<>();
}
