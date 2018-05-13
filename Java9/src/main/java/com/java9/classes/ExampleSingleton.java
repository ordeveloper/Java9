package com.java9.classes;

import java.util.ArrayList;
import java.util.List;

public enum ExampleSingleton {
	
	INSTANCE;
	
	private List<String> studenti = new ArrayList<String>();

	public List<String> getStudenti() {
		return studenti;
	}

	public void setStudenti(List<String> studenti) {
		this.studenti = studenti;
	}
	
	

}
