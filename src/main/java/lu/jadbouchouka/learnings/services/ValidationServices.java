package lu.jadbouchouka.learnings.services;

import javax.annotation.PostConstruct;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.impl.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationServices {

	@Autowired
	private RuntimeService runtimeService;

	@PostConstruct
	public void setUp() {
		System.out.println("");
	}

	public void sizeValidation() {
		System.out.println("Validation de la taille du fichier Ok");
	}

	public void contentValidation() {
		System.out.println("Validation du contenu du fichier Ok");
	}

	public void sendMailSuccess() {

		System.out.println("[Begin] Envoi du mail");

		// Error handling
		errorOccured("3");

		System.out.println("[End] Envoi du mail");
	}

	public void sendMailFailed() {
		String stepNumber = (String) runtimeService.getVariable(Context.getExecutionContext().getExecution().getId(),
				"step");
		System.out.println("Envoi du mail - failed. Error while executing step " + stepNumber);
	}

	private void errorOccured(String stepNumber) {// Error throwing
		Integer retry = (Integer) runtimeService.getVariable(Context.getExecutionContext().getExecution().getId(),
				"retry");
		runtimeService.setVariable(Context.getExecutionContext().getExecution().getId(), "retry", --retry);
		runtimeService.setVariable(Context.getExecutionContext().getExecution().getId(), "step", stepNumber);
		throw new BpmnError("ERROR_SEND_MAIL_FAIL");
	}
}