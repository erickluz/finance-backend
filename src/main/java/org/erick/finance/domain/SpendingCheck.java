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
public class SpendingCheck {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JoinColumn(name = "idSpending")
	@ManyToOne
	private Spending spending;
	@JoinColumn(name = "idSpendingCheckMonth")
	@ManyToOne
	private SpendingCheckMonth spendingCheckMonth;
	private String note;
}
