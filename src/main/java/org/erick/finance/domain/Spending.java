package org.erick.finance.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Spending {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private LocalDateTime date;
	private BigDecimal value;
	@JoinColumn(name = "idCategory")
	@ManyToOne
	private SpendingCategory category;
	@JsonIgnore
	private Short type;
	@JsonIgnore
	@JoinColumn(name = "idSpendingGroup")
	@ManyToOne
	private Spending spendingGroup;
	@JsonIgnore
	@OneToMany(mappedBy = "spendingGroup", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Spending> spendingsInsallments = new ArrayList<>();
	@ManyToOne
	@JoinColumn(name = "idCard")
	private Card card;
	@OneToMany(mappedBy = "spending")
	private List<SpendingCheck> spendingsCheck = new ArrayList<>();
	@OneToMany(mappedBy = "spending")
	private List<SpendingCreditCardSpending> spendingsCreditCardSpending = new ArrayList<>();
	
}
