package lu.jadbouchouka.learnings.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.impl.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lu.jadbouchouka.learnings.dao.CountryRepository;
import lu.jadbouchouka.learnings.dao.PersonRepository;
import lu.jadbouchouka.learnings.model.Country;
import lu.jadbouchouka.learnings.model.Person;

@Component
public class ValidationServices {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private CountryRepository countryRepository;

	private String pathToFolder = "C:\\Users\\jad.bouchouka\\Documents\\ImportsActiviti\\";

	public void sizeValidation() {
		System.out.println("1st step: Trying to validate the size...");
		String fileName = (String) runtimeService.getVariable(Context.getExecutionContext().getExecution().getId(),
				"fileName");

		File file = new File(pathToFolder + fileName);
		if (!file.exists()) {
			errorOccured("1", "ERROR_FILE");
		}
		if (file.length() == 0) {
			errorOccured("1", "ERROR_FILE");
		}
		System.out.println("1st step : File Validation ==> OK");
	}

	public void contentValidation() throws FileNotFoundException {
		System.out.println("2nd step: Trying to validate the content...");
		String fileName = (String) runtimeService.getVariable(Context.getExecutionContext().getExecution().getId(),
				"fileName");

		Scanner input = new Scanner(new File(pathToFolder + fileName));
		while (input.hasNext()) {
			String nextLine = input.nextLine();
			if (nextLine.split(";").length != 2) {
				errorOccured("2", "ERROR_FILE");
			}
		}
		input.close();
		System.out.println("2nd step : File Content ==> OK");
	}

	public void sendMailSuccess() throws FileNotFoundException {

		System.out.println("3rd step: Trying to persist data...");

		String fileName = (String) runtimeService.getVariable(Context.getExecutionContext().getExecution().getId(),
				"fileName");

		Scanner input = new Scanner(new File(pathToFolder + fileName));
		while (input.hasNext()) {
			String nextLine = input.nextLine();
			String[] data = nextLine.split(";");

			Person person = personRepository.findByName(data[0]);
			if (person == null) {
				person = new Person();
				person.setName(data[0]);
				personRepository.save(person);
			}

			Country country = countryRepository.findByName(data[1].toUpperCase());
			if (country == null) {
				country = new Country();
				country.setName(data[1].toUpperCase());
				countryRepository.save(country);
			}

			person.getVisitedCountries().add(country);
			personRepository.save(person);
		}
		input.close();

		System.out.println("3rd step: Data persisted ===> OK");
	}

	public void sendMailFailed() {
		String stepNumber = (String) runtimeService.getVariable(Context.getExecutionContext().getExecution().getId(),
				"step");
		System.out.println("Envoi du mail - failed. Error while executing step " + stepNumber);
	}

	private void errorOccured(String stepNumber, String errorCode) {// Error
																	// throwing
		Integer retry = (Integer) runtimeService.getVariable(Context.getExecutionContext().getExecution().getId(),
				"retry");
		runtimeService.setVariable(Context.getExecutionContext().getExecution().getId(), "retry", --retry);
		runtimeService.setVariable(Context.getExecutionContext().getExecution().getId(), "step", stepNumber);
		throw new BpmnError(errorCode);
	}
}