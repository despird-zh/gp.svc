package com.gp.quickflow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowProcessFactory {
	
	private ServiceLoader<FlowProcess> loader;
	
	static Logger LOGGER = LoggerFactory.getLogger(FlowProcessFactory.class);
	
	private static Map<String, FlowProcess> operationMap;
	
	/**
	 * The auto running snippet 
	 **/
	static{
		new FlowProcessFactory();
	}
	
	/**
	 * Constructor load the flow operations automatically 
	 **/
	private FlowProcessFactory(){
		
		operationMap = new HashMap<String, FlowProcess>();
		try {
            Iterator<FlowProcess> operations = loader.iterator();
            while (operations.hasNext()) {
            	FlowProcess foper = operations.next();
            	operationMap.put(foper.getProcessName(), foper);
            }
        } catch (ServiceConfigurationError serviceError) {
        	LOGGER.error("Fail to load operations", serviceError);
        }
	}
	
	/**
	 * Get the FlowProcess object from map
	 * @param operation the key of flow operation
	 * @return FlowProcess the operation object.
	 **/
	public static FlowProcess getFlowOperation(String operation){
		
		return operationMap.get(operation);
	}
	
	/**
	 * Get the set of keys out of operation map 
	 **/
	public static Set<String> getFlowOperationKeys(){
		
		return operationMap.keySet();
	}
}
