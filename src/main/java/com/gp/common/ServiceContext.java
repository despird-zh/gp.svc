/*******************************************************************************
 * Copyright 2016 Gary Diao - gary.diao@outlook.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.gp.common;

import org.apache.commons.lang.StringUtils;

import com.gp.common.GeneralContext;
import com.gp.info.InfoId;
import com.gp.info.TraceableInfo;
import com.gp.util.DateTimeUtils;

/**
 * This class be used as mandatory parameter across Services and DAOs 
 * it not support audit by default. the audit support be implemented in AuditServiceContext class
 * 
 * @author gary diao
 * @version 0.1 2014-12-12
 **/
public class ServiceContext extends GeneralContext {

	/** the principal  */
	private Principal principal = null;

	/** the workgroup key */
	private InfoId<Long> workgroupId = null;
	
	private static ServiceContext pseudoservice = null;
	
	/** 
	 * constructor with principal
	 **/
	public ServiceContext(Principal principal){
		this.principal = principal;
		this.setAuditable(false);
	}
	
	/**
	 * get principal of context 
	 **/
	public Principal getPrincipal(){
		 
		return this.principal;
	}
	
	/**
	 * set the principal to context 
	 **/
	public void setPrincipal(Principal principal){
		
		this.principal = principal;
	}	
	
	/**
	 * Set the work group key of current context
	 **/
	public void setWorkgroupId(InfoId<Long> workgroupId){
		
		this.workgroupId = workgroupId;
	}
	
	/**
	 * Get the work group key of current context.
	 **/
	public InfoId<Long> getWorkgroupId(){
		
		return this.workgroupId;
	}
	
	/**
	 * begin operation auditing of service context
	 * @param operation
	 * @param verb
	 * @param object
	 * @param predicates
	 **/
	public void beginAudit(String verb, InfoId<?> object, Object predicates){
		
		if(isAuditable())
			beginAudit(principal.getAccount(), verb, object, predicates);
	}


	@Override
	public void close(){

		super.close();
		this.principal = null;
	}
	
	
	/**
	 * For some operation don't need to record the audit data, so feed it 
	 * a pseudo service context to meet certain method.
	 * 
	 * @return ServiceContext<Object> the pseudo service context.
	 **/
	public static ServiceContext getPseudoServiceContext(){
		
		if(pseudoservice == null){
		
			pseudoservice = new ServiceContext(GroupUsers.PSEUDO_USER);
		}
		
		return pseudoservice;
		
	}
	
	/**
	 * For some operation don't need to record the audit data, so feed it 
	 * a pseudo service context to meet certain method.
	 * 
	 * @return ServiceContext<Object> the pseudo service context.
	 **/
	public static ServiceContext getPseudoServiceContext(boolean newone){
		
		if(newone){
						
			return new ServiceContext(GroupUsers.PSEUDO_USER);
		}
		
		return getPseudoServiceContext();
		
	}
	
	/**
	 * For some operation don't need to record the audit data, so feed it 
	 * a pseudo service context to meet certain method.
	 * 
	 * @return ServiceContext<Object> the pseudo service context.
	 **/
	public static ServiceContext getPseudoServiceContext(int sourceId){
		
		Principal principal = GroupUsers.PSEUDO_USER;
		
		return new ServiceContext(principal);
		
	}
    
    /**
     * Amend modifier and modify data 
     * 
     * @param traceinfo the tracing information object
     **/
    public void setTraceInfo(TraceableInfo<?> traceinfo){
    	
    	if(null == traceinfo)
    		return;
    	
    	if(StringUtils.isBlank(traceinfo.getModifier())){
    		
    		traceinfo.setModifier(getPrincipal().getAccount());
    	}
    	if(null == traceinfo.getModifyDate()){
    		
    		traceinfo.setModifyDate(DateTimeUtils.now());
    	}
    }
}
