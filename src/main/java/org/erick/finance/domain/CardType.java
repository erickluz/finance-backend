package org.erick.finance.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum CardType {
	VR			((short) 1, "VR"),
	DEBIT 		((short) 2, "Debit"),
	CREDIT		((short) 3, "Credit"),
	BORROWED	((short) 4, "Borrowed Card");
	
	private static final Map<Short, CardType> cards = new ConcurrentHashMap<>(); 
	
	static {
		for (CardType card : values()) {
			cards.put(card.getId(), card);
		}
	}
	
	private final short id;
	private final String name;
	
	CardType(short id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public short getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static CardType fromId(Short id) {
		return cards.get(id);
	}

}
