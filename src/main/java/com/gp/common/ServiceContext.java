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
public class ServiceContext<A> extends GeneralContext<A> {

	/** the principal  */
	private Principal principal = null;

	/** the workgroup key */
	private InfoId<Long> workgroupId = null;
	
	private static ServiceContext<Object> pseudoservice = null;
	
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
	 * Set the workgroupKey of current context
	 **/
	public void setWorkgroupId(InfoId<Long> workgroupId){
		
		this.workgroupId = workgroupId;
	}
	
	/**
	 * Get the workgroup key of current context.
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
	public static ServiceContext<Object> getPseudoServiceContext(){
		
		if(pseudoservice == null){
		
			pseudoservice = new ServiceContext<Object>(Users.PESUOD_USER);
		}
		
		return pseudoservice;
		
	}
	
	/**
	 * For some operation don't need to record the audit data, so feed it 
	 * a pseudo service context to meet certain method.
	 * 
	 * @return ServiceContext<Object> the pseudo service context.
	 **/
	public static ServiceContext<Object> getPseudoServiceContext(boolean newone){
		
		if(newone){
						
			return new ServiceContext<Object>(Users.PESUOD_USER);
		}
		
		return getPseudoServiceContext();
		
	}
	
	/**
	 * For some operation don't need to record the audit data, so feed it 
	 * a pseudo service context to meet certain method.
	 * 
	 * @return ServiceContext<Object> the pseudo service context.
	 **/
	public static ServiceContext<Object> getPseudoServiceContext(int sourceId){
		
		Principal principal = Users.PESUOD_USER;
		
		return new ServiceContext<Object>(principal);
		
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
