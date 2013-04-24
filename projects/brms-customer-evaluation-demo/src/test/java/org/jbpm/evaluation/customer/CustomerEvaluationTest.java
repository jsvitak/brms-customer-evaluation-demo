package org.jbpm.evaluation.customer;

import java.util.HashMap;
import java.util.Map;


import org.jbpm.test.JbpmJUnitTestCase;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkflowProcessInstance;

/**
 * This is a sample file to launch a process.
 */
public class CustomerEvaluationTest extends JbpmJUnitTestCase {

	private static Integer underAged    = 11;
	private static Integer adultAged    = 25;
	private static Integer richCustomer = 2000; // greater than 999.
	private static Integer poorCutomer  = 2;
	private static KieBase kbase; 
	private static KieSession ksession;
	
	public CustomerEvaluationTest() {
		super(true);
	}
	
	private void setupTestCase() {
		Map<String, ResourceType> kbtypes = new HashMap<String, ResourceType>();
		kbtypes.put("financerules.drl", ResourceType.DRL);
		kbtypes.put("customereval.bpmn2", ResourceType.BPMN2);
		
		kbase = null;
		try {
			kbase = createKnowledgeBase(kbtypes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ksession = createKnowledgeSession(); 
	}
	
	@Test
	public void underagedCustomerEvaluationTest() {

		setupTestCase();

		// optional: setup logging.
		//KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newThreadedFileLogger(ksession, "CustomerEvaluationUnderaged", 1000);
	
		// setup of a Person and Request.
		Person underagedEval = getUnderagedCustomer();
		Request richEval = getRichCustomer();
		ksession.insert(underagedEval);

		// Map to be passed to the startProcess.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("person", underagedEval);
		params.put("request", richEval);
		
		// Fire it up!
		System.out.println("=========================================");
		System.out.println("= Starting Process Underaged Test Case. =");
		System.out.println("=========================================");

		WorkflowProcessInstance processInstance = (WorkflowProcessInstance) ksession.startProcess("org.jbpm.customer-evaluation", params);
		ksession.insert(processInstance);
		ksession.fireAllRules();
				
		// Finished, clean up the logger.
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
		assertNodeTriggered(processInstance.getId(), "Underaged");
		//logger.close();
		ksession.dispose();
	}

	@Test
	public void adultCustomerEvaluationTest() {

		setupTestCase();
		
		// optional: setup logging.
		//KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newThreadedFileLogger(ksession, "CustomerEvaluationPoorAdult", 1000);
	
		// setup of a Person and Request.
		Person adultEval = getAdultCustomer();
		Request poorEval = getPoorCustomer();
		ksession.insert(adultEval);

		// Map to be passed to the startProcess.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("person", adultEval);
		params.put("request", poorEval);
		
		// Fire it up!
		System.out.println("==========================================");
		System.out.println("= Starting Process Poor Adult Test Case. =");
		System.out.println("==========================================");

		WorkflowProcessInstance processInstance = (WorkflowProcessInstance) ksession.startProcess("org.jbpm.customer-evaluation", params);
		ksession.insert(processInstance);
		ksession.fireAllRules();
				
		// Finished, clean up the logger.
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
		assertNodeTriggered(processInstance.getId(), "End Poor Customer");
		//logger.close();
		ksession.dispose();
	}

	@Test
	public void richCustomerEvaluationTest() {

		setupTestCase();
		
		// optional: setup logging.
		//KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newThreadedFileLogger(ksession, "CustomerEvaluationRichAdult", 1000);
	
		// setup of a Person and Request.
		Person adultEval = getAdultCustomer();
		Request richEval = getRichCustomer();
		ksession.insert(adultEval);

		// Map to be passed to the startProcess.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("person", adultEval);
		params.put("request", richEval);
		
		// Fire it up!
		System.out.println("==========================================");
		System.out.println("= Starting Process Rich Adult Test Case. =");
		System.out.println("==========================================");
		WorkflowProcessInstance processInstance = (WorkflowProcessInstance) ksession.startProcess("org.jbpm.customer-evaluation", params);
		ksession.insert(processInstance);
		ksession.fireAllRules();
				
		// Finished, clean up the logger.
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
		assertNodeTriggered(processInstance.getId(), "End Rich Customer");
		//logger.close();
		ksession.dispose();
	}

	
	@Test
	public void emptyRequestCustomerEvaluationTest() {

		setupTestCase();
		
		// optional: setup logging.
		//KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newThreadedFileLogger(ksession, "CustomerEvaluationEmptyRequest", 1000);
	

		// Map to be passed to the startProcess is intentionally empty.
		Map<String, Object> params = new HashMap<String, Object>();
		
		// Fire it up!
		System.out.println("=============================================");
		System.out.println("= Starting Process Empty Request Test Case. =");
		System.out.println("=============================================");
		WorkflowProcessInstance processInstance = (WorkflowProcessInstance) ksession.startProcess("org.jbpm.customer-evaluation", params);
		ksession.insert(processInstance);
		ksession.fireAllRules();
				
		// Finished, clean up the logger.
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
		assertNodeTriggered(processInstance.getId(), "Underaged");
		//logger.close();
		ksession.dispose();
	}


	/**
	 * Provide an under aged person.
	 * 
	 * @return org.jbpm.evaluation.customer.Person
	 */
	private Person getUnderagedCustomer() {
		Person person = new Person("erics", "Eric D. Schabell");
		person.setAge(underAged);
		return person;
	}

	
	/**
	 * Provide an of aged person.
	 * 
	 * @return org.jbpm.evaluation.customer.Person
	 */
	private Person getAdultCustomer() {
		Person person = new Person("erics", "Eric D. Schabell");
		person.setAge(adultAged);
		return person;
	}

	/**
	 * Provide a poor person in the request.
	 * 
	 * @return org.jbpm.evaluation.customer.Request
	 */
	private Request getPoorCustomer() {
		Request request = new Request("1");
		request.setPersonId("erics");
		request.setAmount(poorCutomer);
		return request;
	}

	/**
	 * Provide a rich person in the request.
	 * 
	 * @return org.jbpm.evaluation.customer.Request
	 */
	private Request getRichCustomer() {
		Request request = new Request("1");
		request.setPersonId("erics");
		request.setAmount(richCustomer);
		return request;
	}
}