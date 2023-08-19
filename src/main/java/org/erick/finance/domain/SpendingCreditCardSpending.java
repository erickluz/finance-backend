package org.erick.finance.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SpendingCreditCardSpending {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JoinColumn(name = "idCreditCardSpending")
	@ManyToOne
	private CreditCardSpending creditCardSpending;
	@JoinColumn(name = "idSpending")
	@ManyToOne
	private Spending spending;
	@JoinColumn(name = "idSpendingCheckMonth")
	@ManyToOne
	private SpendingCheckMonth spendingCheckMonth;
	@Override
	public String toString() {
		return "SpendingCreditCardSpending [id=" + id + ", creditCardSpending=" + creditCardSpending.getId() + ", spending="
				+ spending.getId() + ", spendingCheckMonth=" + spendingCheckMonth.getId() + "]";
	}
	
	
}
