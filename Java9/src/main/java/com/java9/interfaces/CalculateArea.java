package com.java9.interfaces;

public interface  CalculateArea {

	public default double rettangolo(double base, double altezza) {
		return (base * altezza)/2;
	}
}
