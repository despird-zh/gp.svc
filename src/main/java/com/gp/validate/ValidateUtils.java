package com.gp.validate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;

import com.gp.common.GeneralConfig;
import com.gp.common.GeneralConstants;
import com.gp.common.SystemOptions;
import com.gp.validate.ValidateMessage;

/**
 * This utils implements base on hibernate validation, 
 * the msgSource hold the message resource.
 * 
 * @author gary diao
 * @version 0.1 2015-1-1
 * 
 **/
public class ValidateUtils {
	
	/**
	 * singleton instance
	 **/
	private static ValidateUtils instance;
	
	private MessageSource msgSource = null;
	
	/**
	 * Hide the ValidationUtils constructor.
	 * 
	 **/
	private ValidateUtils(){
		
		String basenameStr = GeneralConfig.getString(SystemOptions.VALID_MSG_RESOURCES);
		if(StringUtils.isNotBlank(basenameStr)){
			String[] basenames = basenameStr.split(GeneralConstants.KVPAIRS_SEPARATOR);
			ResourceBundleMessageSource msgSource = new ResourceBundleMessageSource();
			msgSource.setBasenames(basenames);
			msgSource.setDefaultEncoding("UTF-8");
		}
	};

	/**
	 * Self complete initialization 
	 **/
	static {
		instance = new ValidateUtils();		
	}
	
	/**
	 * hold validator for different locales. 
	 **/
	private Map<Locale, Validator > vmap = new HashMap<Locale, Validator >();
	
	/**
	 * Get validator from map by locale.
	 * 
	 * @param locale the locale object
	 **/
	public static Validator getValidator(Locale locale){
		
		ResourceBundleMessageInterpolator msgInterpolator = null;
		if(instance.msgSource != null){
			msgInterpolator = new ResourceBundleMessageInterpolator(new MessageSourceResourceBundleLocator(instance.msgSource));
		}else{
			msgInterpolator = new ResourceBundleMessageInterpolator();
		}
		Validator validator = instance.vmap.get(locale);
		
		if(validator == null){
			
			validator = Validation.byProvider(HibernateValidator.class)
	                .configure()
	                .messageInterpolator(
	                    new LocaleMessageInterpolator(msgInterpolator,locale)
	                )
	                .buildValidatorFactory()
	                .getValidator();
			
			instance.vmap.put(locale, validator);
		}
		
		return validator;
	}
	
	/**
	 * Validate the bean object with specified locale
	 * 
	 * @param locale the locale object
	 * @param object the bean object
	 *  
	 **/
	public static <T> Set<ValidateMessage> validate(Locale locale, T object) {
		
		Set<ValidateMessage> result = new HashSet<ValidateMessage>();
		
		Validator validator = getValidator(locale);
		
		Set<ConstraintViolation<T>> set = validator.validate(object, Default.class);
		if (CollectionUtils.isNotEmpty(set)) {
			
			for (ConstraintViolation<T> cv : set) {
				
				ValidateMessage vm = new ValidateMessage(
						cv.getPropertyPath().toString(), 
						cv.getMessage());
				
				result.add(vm);
			}
		}
		
		return result;
	}

	/**
	 * validate the property of bean 
	 * 
	 * @param locale the locale object
	 * @param object the bean object
	 * @param propertyName the name of bean property
	 * 
	 **/
	public static <T> Set<ValidateMessage> validateProperty(Locale locale, T object, String ...propertyName) {
		
		Set<ValidateMessage> result = new HashSet<ValidateMessage>();
		
		Validator validator = getValidator(locale);
		for(String propname : propertyName){
			
			Set<ConstraintViolation<T>> set = validator.validateProperty(object, propname, Default.class);
			
			if (CollectionUtils.isNotEmpty(set)) {
				for (ConstraintViolation<T> cv : set) {
					ValidateMessage vm = new ValidateMessage(
							cv.getPropertyPath().toString(), 
							cv.getMessage());
					
					result.add(vm);
				}
			}
		}
		return result;
	}

}
