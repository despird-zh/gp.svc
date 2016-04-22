package com.gp.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gp.common.GeneralConfig;
import com.gp.common.ServiceContext;
import com.gp.common.SystemOptions;
import com.gp.exception.ServiceException;
import com.gp.info.SysOptionInfo;
import com.gp.svc.SystemService;

/**
 * detect configuration setting in following sequence.
 * <ol>
 * 	<li>Find in META-INF/core-configuration file</li>
 *  <li>Find in core-configuration file</li>
 *  <li>Find in gp_sys_options</li>
 * </ol>
 * 
 * @author gary diao
 * @version 0.1 2015-1-1
 * 
 **/
public class ConfigSettingUtils {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ConfigSettingUtils.class);
	
	private static SystemService systemservice = null;
	
	public static void setSystemService( SystemService systemservice){
		ConfigSettingUtils.systemservice = systemservice;
	}
	
	/**
	 * get the system option value
	 * 
	 * @param key the option key
	 * @return String the option value
	 **/
	public static String getSystemOption(String key){
		
		String value = GeneralConfig.getString(key);
		
		if(StringUtils.isBlank(value) && systemservice != null){
			ServiceContext<Object> svcctx = ServiceContext.getPseudoServiceContext();
			try {
			
				SysOptionInfo soi = systemservice.getOption(svcctx, key);
				value = soi != null ? soi.getOptionValue() : null;
			
			} catch (ServiceException e) {
				LOGGER.error("fail to fetch the system option info", e );
			}
		}
		return value;		
	}

}
