package lu.jadbouchouka.learnings.webapp;

import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lu.jadbouchouka.learnings.dao.CountryRepository;
import lu.jadbouchouka.learnings.dao.PersonRepository;
import lu.jadbouchouka.learnings.model.Country;
import lu.jadbouchouka.learnings.model.Person;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class MainController {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private PersonRepository personRepository;

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/sendFile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void start(@RequestParam("fileName") String fileName) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("retry", 3);
		variables.put("step", 1);
		variables.put("fileName", fileName);
		runtimeService.startProcessInstanceByKey("enhacementForInputHall", variables);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/getCountry", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Country getCountry(@RequestParam("countryName") String countryName) {
		return countryRepository.findByName(countryName);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/getAllCountries", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Country> getAllCountries() {
		List<Country> countries = (List<Country>) countryRepository.findAll();
		for (Country country : countries) {
			country.setVisitors(getAllPersonsWhoVisitedTheCountry(country.getName()));
		}
		return countries;
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/getAllPersonsWhoVisitedTheCountry", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Set<Person> getAllPersonsWhoVisitedTheCountry(@RequestParam("countryName") String countryName) {
		return personRepository.findAllPersonsWhoVisitedTheCountry(countryRepository.findByName(countryName));
	}

}