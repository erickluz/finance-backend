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
	@JoinColumn(name = "idSpendingCheck")
	@ManyToOne
	private SpendingCheckMonth spendingCheck;
}