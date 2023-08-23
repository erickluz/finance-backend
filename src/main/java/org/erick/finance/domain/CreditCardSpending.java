package org.erick.finance.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@JsonIgnore
	private Short type;
	@ColumnDefault("false")
	private Boolean isJustified;
	private String justification;
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "idCreditCardSpendingGrouping")
	private CreditCardSpending creditCardSpendingGrouping;
	@JsonIgnore
	@OneToMany(mappedBy = "creditCardSpendingGrouping", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	private List<CreditCardSpending> creditCardSpendingsChildren = new ArrayList<>();
	@JsonIgnore
	@JoinColumn(name = "idCreditCardBill")
	@ManyToOne
	private CreditCardBill creditCardBill;
	@JsonIgnore
	@OneToMany(mappedBy = "creditCardSpending")
	private List<SpendingCreditCardSpending> spendingCreditCardSpending = new ArrayList<>(); 

}
