package org.erick.finance.util;

import java.util.concurrent.ConcurrentHashMap;

public enum Month {
	
	JANUARY(	1,  "Jan", "January"),
	FEBRUARY(	2,  "Feb", "February"),
	MARCH(		3,  "Mar", "March"),
	APRIL(		4,  "Apr", "April"),
	MAY(		5,  "May", "May"),
	JUNE(		6,  "Jun", "June"),
	JULY(		7,  "Jul", "July"),
	AUGUST(		8,  "Aug", "August"),
	SEPTEMBER(	9,  "Sep", "September"),
	OCTOBER(	10, "Oct", "October"),
	NOVEMBER(	11, "Nov", "November"),
	DECEMBER(	12, "Dec", "December");

	private static final ConcurrentHashMap<Integer, Month> months = new ConcurrentHashMap<>();
	private final Integer monthNumber;
	private final String shortName;
	private final String name;
	
	static {
		for(Month month : values()) {
			months.put(month.monthNumber, month);
		}
	}
	
	Month(Integer monthNumber, String shortName, String name) {
		this.monthNumber = monthNumber;
		this.shortName = shortName;
		this.name = name;
	}

	public Integer getMonthNumber() {
		return monthNumber;
	}

	public String getShortName() {
		return shortName;
	}

	public String getName() {
		return name;
	}
	
	public static Month fromNumber(Integer monthNumber) {
		return months.get(monthNumber);
	}
	
}
