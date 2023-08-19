package org.erick.finance.dto;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.erick.finance.domain.CreditCardSpending;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreditCardFileDTO {
	private String date;
	private String description;
	private String value;
	private String part;
	
	public CreditCardSpending toCreditCardSpending() {
		BigDecimal value = parseRawValue();
		LocalDateTime date = LocalDateTime.parse(this.date + " 00:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy[ HH:mm:ss]"));
		return new CreditCardSpending(null, this.description, value, date, this.part, null, null);
	}

	private BigDecimal parseRawValue() {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		//symbols.setGroupingSeparator(',');
		symbols.setDecimalSeparator(',');
		String pattern = "#,##0.0#";
		DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);
		try {
			String rawValue = this.value.replace("R$", "").trim();
			return (BigDecimal) decimalFormat.parse(rawValue);
		} catch (ParseException e) {
			System.out.println("File import error. Error: " + e.getMessage());
			return BigDecimal.ZERO;
		}
	}
	
}