package lu.jadbouchouka.learnings.model;

import javax.persistence.Entity;

@Entity
public class Country {

	private int id;
	
	private String name;
	
	private String capital;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}
	
	
}
