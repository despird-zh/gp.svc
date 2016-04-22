package com.gp.svc.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.Users;
import com.gp.dao.IdSettingDAO;
import com.gp.exception.ServiceException;
import com.gp.info.IdSettingInfo;
import com.gp.info.Identifier;
import com.gp.info.InfoId;
import com.gp.svc.IdService;

@Service("idService")
public class IdServiceImpl implements IdService{

	public static Logger LOGGER = LoggerFactory.getLogger(IdServiceImpl.class);
	
	@Autowired
	private IdSettingDAO idsettingdao;
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public <T> InfoId<T> generateId(String modifier, Identifier idkey, Class<T> type) throws ServiceException{
		InfoId<?> newId = null;
		IdSettingInfo idinfo = idsettingdao.queryByIdKey(idkey);
		// string type
		if(String.class.equals(type)){
			String prefix = idinfo.getPrefix() == null ? StringUtils.EMPTY : idinfo.getPrefix().trim();
			String padstr = idinfo.getPadChar() == null ? StringUtils.EMPTY : idinfo.getPadChar().trim();
			Long currval = idinfo.getCurrValue() ;
			
			String id = StringUtils.leftPad(String.valueOf(currval), idinfo.getLength()- prefix.length(), padstr);
			
			newId = new InfoId<String>(idkey, prefix + id);
			
		}else if(Long.class.equals(type)) {
			
			Long currval = idinfo.getCurrValue() ;
			newId = new InfoId<Long>(idkey,currval);	
		}else if(Integer.class.equals(type)) {
			
			Integer currval = (int)(long)idinfo.getCurrValue() ;
			newId = new InfoId<Integer>(idkey,currval);	
		}else{
			
			throw new UnsupportedOperationException("Only support String/Long/Integer type id");
		}
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("idkey : {} / modify date : {} / current : {} ", new Object[]{idkey, idinfo.getModifyDate(),idinfo.getCurrValue()});
		
		Long nextValue = idinfo.getCurrValue() + idinfo.getStepIncrement();			
		idsettingdao.updateByIdKey(modifier, idkey, nextValue);
		
		return (InfoId<T>)newId;
	
	}
	
	@Override
	public <T> InfoId<T> generateId( Identifier idkey,Class<T> type) throws ServiceException {
		
		return generateId(Users.PESUOD_USER.getAccount(),idkey, type);
	}

}
