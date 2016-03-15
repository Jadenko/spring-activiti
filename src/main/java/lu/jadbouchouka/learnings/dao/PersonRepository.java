package lu.jadbouchouka.learnings.dao;

import org.springframework.data.repository.CrudRepository;

import lu.jadbouchouka.learnings.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer>{

	Person findByName(String name);
}
