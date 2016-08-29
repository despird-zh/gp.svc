package com.gp.quickflow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowOperationFactory {
	
	private ServiceLoader<FlowOperation> loader;
	
	static Logger LOGGER = LoggerFactory.getLogger(FlowOperationFactory.class);
	
	private static Map<String, FlowOperation> operationMap;
	
	/**
	 * The auto running snippet 
	 **/
	static{
		new FlowOperationFactory();
	}
	
	/**
	 * Constructor load the flow operations automatically 
	 **/
	private FlowOperationFactory(){
		
		operationMap = new HashMap<String, FlowOperation>();
		try {
            Iterator<FlowOperation> operations = loader.iterator();
            while (operations.hasNext()) {
            	FlowOperation foper = operations.next();
            	operationMap.put(foper.getOperation(), foper);
            }
        } catch (ServiceConfigurationError serviceError) {
        	LOGGER.error("Fail to load operations", serviceError);
        }
	}
	
	/**
	 * Get the FlowOperation object from map
	 * @param operation the key of flow operation
	 * @return FlowOperation the operation object. 
	 **/
	public static FlowOperation getFlowOperation(String operation){
		
		return operationMap.get(operation);
	}
	
	/**
	 * Get the set of keys out of operation map 
	 **/
	public static Set<String> getFlowOperationKeys(){
		
		return operationMap.keySet();
	}
}
