package org.erick.finance.domain;

import java.util.concurrent.ConcurrentHashMap;

public enum TypeSpending {
	NORMAL		((short) 1, "Normal"),
	GROUPING	((short) 2, "Grouping"),
	PART		((short) 3, "Part");
	private static final ConcurrentHashMap<Short, TypeSpending> types = new ConcurrentHashMap<>();  
	private final Short code;
	private final String name;
	
	static {
		for (TypeSpending type : values()) {
			types.put(type.getCode(), type);
		}
	}

	private TypeSpending(Short code, String name) {
		this.code = code;
		this.name = name;
	}

	public Short getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
	public static TypeSpending fromCode(Short code) {
		return types.get(code);
	}
	
}
