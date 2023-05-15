package org.erick.finance.service;

import java.util.List;

import org.erick.finance.domain.Card;
import org.erick.finance.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {
	@Autowired
	private CardRepository rep;

	public List<Card> listAll() {
		return rep.findAll();
	}

	public Card save(Card card) {
		return rep.save(card);
	}

	public void delete(Long id) {
		rep.deleteById(id);
	}

	public Card findById(Long id) {
		return rep.findById(id).orElse(null);
	}
}
