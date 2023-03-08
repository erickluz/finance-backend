package org.erick.finance.domain;

public enum TypeSpending {
	NORMAL		((short) 1, "Normal"),
	GROUPING	((short) 2, "Grouping"),
	PART		((short) 3, "Part");
	
	private final Short code;
	private final String name;
	
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
	
}
