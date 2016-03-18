package lu.jadbouchouka.learnings.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable=false, length=30)
	private String name;
	
	private Date birthday;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="person_visitedCountries", joinColumns=@JoinColumn(name="person_id"), inverseJoinColumns=@JoinColumn(name="country_id"))
	private Set<Country> visitedCountries = new HashSet<>();

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

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Set<Country> getVisitedCountries() {
		return visitedCountries;
	}

	public void setVisitedCountries(Set<Country> visitedCountries) {
		this.visitedCountries = visitedCountries;
	}
}
