package org.erick.finance.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpendingCheckAssociationDTO {
	private ItemCheckSpendingDTO spending;
	private ItemCheckSpendingDTO creditCardSpending;

	public SpendingCheckAssociationDTO(Long spendigId, String spendingName, LocalDateTime spendingDate, Long idSpendingCheck,
			Boolean isSpendingCardChkByFile, String spendingCardIssuer, BigDecimal spendingValue, Long ccsId, String ccsDescription,
			LocalDateTime ccsDate, Long ccsIdSpendingCreditCardSpending, Boolean ccsIsCardChkByFile, String ccsCardIssuer, BigDecimal ccsValue, Short typeSpending, Boolean isJustified) {

		Boolean isAssociable = (typeSpending != 4) ? (isSpendingCardChkByFile == null) ? false : isSpendingCardChkByFile : true;
		
		String dateSpending = spendingDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		Boolean isSpendingChk = (!isAssociable) ? (idSpendingCheck != null) : false; 
		this.spending = new ItemCheckSpendingDTO(spendigId.toString(), spendingName, dateSpending, spendingCardIssuer, spendingValue.toString(), isAssociable, isSpendingChk, false);

		String dateCreditCardSpendig = (ccsDate != null) ? ccsDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
		String sccsId = (ccsId != null) ? ccsId.toString() : null; 
		String sccsValue = (ccsValue != null) ? ccsValue.toString() : null;
		this.creditCardSpending = new ItemCheckSpendingDTO(sccsId, ccsDescription, dateCreditCardSpendig, ccsCardIssuer, sccsValue, ccsIsCardChkByFile, false, isJustified);
	}

}
