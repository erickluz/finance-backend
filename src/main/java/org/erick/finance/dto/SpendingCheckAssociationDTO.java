package org.erick.finance.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpendingCheckAssociationDTO {
	private ItemCheckSpendingDTO spending;
	private ItemCheckSpendingDTO creditCardSpending;

	public SpendingCheckAssociationDTO(Long spendigId, String spendingName, LocalDateTime spendingDate, Long idSpendingCheck,
			Boolean isSpendingCardChkByFile, String spendingCardIssuer, BigDecimal spendingValue, Long ccsId, String ccsDescription,
			LocalDateTime ccsDate, Long ccsIdSpendingCreditCardSpending, Boolean ccsIsCardChkByFile, String ccsCardIssuer, BigDecimal ccsValue) {

		isSpendingCardChkByFile = (isSpendingCardChkByFile == null) ? false : isSpendingCardChkByFile;
		String dateSpending = spendingDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		Boolean isSpendingChk = (!isSpendingCardChkByFile) ? (idSpendingCheck != null) : false; 
		this.spending = new ItemCheckSpendingDTO(spendigId.toString(), spendingName, dateSpending, spendingCardIssuer, spendingValue.toString(), isSpendingCardChkByFile, isSpendingChk);

		String dateCreditCardSpendig = (ccsDate != null) ? ccsDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
		String sccsId = (ccsId != null) ? ccsId.toString() : null; 
		String sccsValue = (ccsValue != null) ? ccsValue.toString() : null;
		this.creditCardSpending = new ItemCheckSpendingDTO(sccsId, ccsDescription, dateCreditCardSpendig, ccsCardIssuer, sccsValue, ccsIsCardChkByFile, false);
	}
	
	public SpendingCheckAssociationDTO(ItemCheckSpendingDTO spendingDTO, ItemCheckSpendingDTO creditCardSpending) {
		this.spending = spendingDTO;
		this.creditCardSpending = creditCardSpending;
	}
}
