package org.erick.finance.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardSpending {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String description;
	private BigDecimal value;
	private LocalDateTime date;
	private String part;
	@JoinColumn(name = "idCreditCardBill")
	@ManyToOne
	private CreditCardBill creditCardBill;
	@OneToMany(mappedBy = "creditCardSpending")
	private List<SpendingCreditCardSpending> spendingCreditCardSpending = new ArrayList<>(); 

}
