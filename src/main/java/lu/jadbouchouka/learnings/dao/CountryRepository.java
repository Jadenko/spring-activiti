package lu.jadbouchouka.learnings.dao;

import org.springframework.data.repository.CrudRepository;

import lu.jadbouchouka.learnings.model.Country;

public interface CountryRepository extends CrudRepository<Country, Integer> {

	public Country findByName(String name);
}
