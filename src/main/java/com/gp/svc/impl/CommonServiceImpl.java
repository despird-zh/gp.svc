package com.gp.svc.impl;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.Users;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.IdSettingDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.info.FlatColLocator;
import com.gp.info.IdSettingInfo;
import com.gp.info.Identifier;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;

@Service("commonService")
public class CommonServiceImpl implements CommonService{

	public static Logger LOGGER = LoggerFactory.getLogger(CommonServiceImpl.class);
	
	private ReentrantLock lock = new ReentrantLock();  
	
	@Autowired
	private IdSettingDAO idsettingdao;
	
	@Autowired
	private PseudoDAO pseudodao;
	
	@SuppressWarnings("unchecked")
	@Transactional(value=ServiceConfigurer.TRNS_MGR,propagation = Propagation.REQUIRES_NEW )
	@Override
	public <T> InfoId<T> generateId(String modifier, Identifier idkey, Class<T> type) throws ServiceException{
		InfoId<?> newId = null;
		
		try{
			lock.lock();
		
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
		}finally{
			lock.unlock();
		}
		return (InfoId<T>)newId;
	
	}
	
	@Override
	public <T> InfoId<T> generateId( Identifier idkey,Class<T> type) throws ServiceException {
		
		return generateId(Users.PESUOD_USER.getAccount(),idkey, type);
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR)
	@Override
	public Integer update(InfoId<?> id, Map<FlatColLocator, Object> fields) throws ServiceException{
		
		try{
			return pseudodao.update(id, fields);
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail update data.", dae);
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR)
	@Override
	public Integer update(InfoId<?> id, FlatColLocator col, Object val) throws ServiceException{
		try{
			return pseudodao.update(id, col, val);
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail update data.", dae);
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR)
	@Override
	public Integer update(InfoId<?> id, FlatColLocator[] col, Object[] val) throws ServiceException{
		try{
			return pseudodao.update(id, col, val);
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail update data.", dae);
		}
	}

}
