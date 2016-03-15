package lu.jadbouchouka.learnings;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, classes={SpringConf.class})
public class ProcessHumanAndServiceTaskExampleTest {
	private String filename = "C:/Users/jad.bouchouka/MyOwnGitRepo/spring-activit/spring-activit-webapp/src/main/resources/processes/EnhacementForInputHall.bpmn";
	/**
	 * Inject repository service
	 */
	@Resource
	private RepositoryService repositoryService;
	/**
	 * Inject runtime service
	 */
	@Resource
	private RuntimeService runtimeService;

	/**
	 * Inject task service
	 */
	@Resource
	private TaskService taskService;

	@Test
	public void startProcess() throws Exception {

		/*
		 * Deploy the process
		 */
		repositoryService.createDeployment().enableDuplicateFiltering()
				.addInputStream("HumanAndServiceTaskExample.bpmn20.xml", new FileInputStream(filename)).deploy();

		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("processStartedBy", "Dhananjay");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("HumanAndServiceTaskExample",
				variableMap);

		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
	}

	@Test
	public void claimAndCompleteHumanTask() throws Exception {
		List<Task> tasks = taskService.createTaskQuery().processDefinitionKey("HumanAndServiceTaskExample")
				.taskDefinitionKey("HumanTaskExample").list();
		for (Task task : tasks) {
			taskService.claim(task.getId(), "DJ");
			Map<String, Object> variableMap = new HashMap<String, Object>();
			variableMap.put("HumanTaskCompletedBy", "DJ");
			taskService.complete(task.getId(), variableMap);
		}
	}
	
	@Test
	public void test() throws FileNotFoundException{
		repositoryService.createDeployment().enableDuplicateFiltering()
		.addInputStream("EnhacementForInputHall.bpmn20.xml", new FileInputStream(filename)).deploy();
		
		Map<String, Object> variables = new HashMap<String, Object>();
    	variables.put("retry", 3);
    	variables.put("step", 1);
        runtimeService.startProcessInstanceByKey("enhacementForInputHall",variables);
	}
}
