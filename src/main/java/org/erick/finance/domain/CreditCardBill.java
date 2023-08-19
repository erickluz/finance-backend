package org.erick.finance.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
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
public class CreditCardBill {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private LocalDateTime dueDate;
	private LocalDateTime uploadFileDate;
	@ManyToOne
	@JoinColumn(name = "idCard", unique = true)
	private Card card;
	@OneToMany(mappedBy = "creditCardBill")
	private List<CreditCardSpending> creditCardSpending = new ArrayList<>();
}
