package com.gp.quickflow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowOperationFactory {
	
	private ServiceLoader<FlowOperation> loader;
	
	static Logger LOGGER = LoggerFactory.getLogger(FlowOperationFactory.class);
	
	private static Map<String, FlowOperation> operationMap;
	
	static{
		new FlowOperationFactory();
	}
	
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
	
	public static FlowOperation getFlowOperation(String operation){
		
		return operationMap.get(operation);
	}
}
