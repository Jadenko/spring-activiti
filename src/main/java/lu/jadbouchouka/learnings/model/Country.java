package lu.jadbouchouka.learnings.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Country {

	@Id
	@SequenceGenerator(name="idGeneratorForCountry",sequenceName="idSequenceForCountry")
	@GeneratedValue(strategy = GenerationType.AUTO,generator="idGeneratorForCountry")
	private int id;

	@Column(nullable=false, length=20)
	private String name;

	@Column(length=20)
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
