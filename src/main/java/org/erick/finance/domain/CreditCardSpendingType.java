package org.erick.finance.domain;

import java.util.concurrent.ConcurrentHashMap;

public enum CreditCardSpendingType {
	NORMAL			((short) 1, "Normal"),
	GROUPING		((short) 2, "Grouping Part");
	private static final ConcurrentHashMap<Short, CreditCardSpendingType> types = new ConcurrentHashMap<>();  
	private final Short code;
	private final String name;
	
	static {
		for (CreditCardSpendingType type : values()) {
			types.put(type.getCode(), type);
		}
	}

	private CreditCardSpendingType(Short code, String name) {
		this.code = code;
		this.name = name;
	}

	public Short getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
	public static CreditCardSpendingType fromCode(Short code) {
		return types.get(code);
	}
	
}
