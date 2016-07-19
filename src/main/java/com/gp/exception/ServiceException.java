package com.gp.exception;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

public class ServiceException  extends BaseException{

	private static final long serialVersionUID = 1L;
	
	private static Map<Locale, ResourceBundle> svc_bundles = new HashMap<Locale, ResourceBundle>();

	public ServiceException(String errorcode,Object ...param){
		this(Locale.getDefault(),errorcode, param);
	}
	
    public ServiceException(String errorcode, Throwable cause,Object ...param) {
        this(Locale.getDefault(), errorcode, cause, param);
    }
    
	public ServiceException(Locale locale, String errorcode, Object... param) {
		super(locale,errorcode, param);
	}
	
    public ServiceException(Locale locale, String errorcode, Throwable cause,Object ...param) {
        super(locale, errorcode, cause, param);
    }
    
    public ServiceException(Throwable cause) {
        super(cause);
    }
    
    @Override
	protected String findMessage(Locale locale, String errorcode,Object ... param){
		
		ResourceBundle rb = svc_bundles.get(locale);
		if(rb == null){
			rb = loadResourceBundle(locale, ServiceException.class);
			svc_bundles.put(locale, rb);
		}
		String messagePattern = null;
		if(rb == null || !rb.containsKey(errorcode)){ 
			messagePattern = errorcode ;
			matched = false;
			// try to find from super class.
			return super.findMessage(locale, errorcode, param);
		}else{ 
			rb.getString(errorcode);
			matched = true;
		}
		
		return MessageFormat.format(messagePattern, param);
	}

}
