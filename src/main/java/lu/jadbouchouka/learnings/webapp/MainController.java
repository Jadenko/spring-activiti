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

import java.util.HashMap;
import java.util.Map;

@RestController
public class MainController {
	
	@Autowired
	private RuntimeService runtimeService;

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/sendFile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void start(@RequestParam("fileName") String fileName) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("retry", 3);
		variables.put("step", 1);
		variables.put("fileName", fileName);
		runtimeService.startProcessInstanceByKey("enhacementForInputHall", variables);
	}

}