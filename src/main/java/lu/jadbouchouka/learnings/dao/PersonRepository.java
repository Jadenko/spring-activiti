package lu.jadbouchouka.learnings.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import lu.jadbouchouka.learnings.model.Country;
import lu.jadbouchouka.learnings.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {

	Person findByName(String name);

	@Query("SELECT p FROM Person p WHERE ?1 MEMBER OF p.visitedCountries")
	Set<Person> findAllPersonsWhoVisitedTheCountry(Country country);
}
