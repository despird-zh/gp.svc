package com.gp.svc.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.BinaryDAO;
import com.gp.dao.PseudoDAO;
import com.gp.dao.StorageDAO;
import com.gp.exception.ServiceException;
import com.gp.info.BinaryInfo;
import com.gp.info.InfoId;
import com.gp.info.StorageInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.pagination.PaginationHelper;
import com.gp.pagination.PaginationInfo;
import com.gp.svc.StorageService;
import com.gp.util.StorageUtils;

@Service("storageService")
public class StorageServiceImpl implements StorageService{

	Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);
	
	@Autowired
	private StorageDAO storagedao;
		
	@Autowired
	private BinaryDAO binarydao;
	
	@Autowired
	private PseudoDAO pseudodao;

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean newBinary(ServiceContext svcctx, BinaryInfo binary) throws ServiceException {

		try{
			InfoId<Integer> storageId = IdKey.STORAGE.getInfoId(binary.getStorageId());
			String storeLoc = StorageUtils.toURIStr(storageId, binary.getInfoId().getId(), binary.getFormat());
			binary.setStoreLocation(storeLoc);
			svcctx.setTraceInfo(binary);
			return binarydao.create(binary) > 0;
		}catch(DataAccessException dae){
			
			throw new ServiceException("fail create binary record",dae);
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public BinaryInfo getBinaryByHash(ServiceContext svcctx, String hashstr) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public BinaryInfo getBinary(ServiceContext svcctx, InfoId<Long> id) throws ServiceException {
		try{			
			return binarydao.query(id);
		}catch(DataAccessException dae){
			
			throw new ServiceException("fail query binary record",dae);
		}
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean removeBinary(ServiceContext svcctx, InfoId<Long> id) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean newStorage(ServiceContext svcctx, StorageInfo storage) throws ServiceException {
		try{
			svcctx.setTraceInfo(storage);
			return storagedao.create(storage) > 0;	
		}catch(DataAccessException dae){
			throw new ServiceException("fail query instance", dae);
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public StorageInfo getStorage(ServiceContext svcctx, InfoId<Integer> id) throws ServiceException {
		try{
			return storagedao.query(id);
		}catch(DataAccessException dae){
			throw new ServiceException("fail query instance", dae);
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public boolean existStorage(ServiceContext svcctx, String storagename) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public void updateStorage(ServiceContext svcctx, InfoId<Integer> id, int used) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean removeStorage(ServiceContext svcctx, InfoId<Integer> id) throws ServiceException {
		try{
			int cnt = storagedao.delete(id);
			return cnt >0 ;
		}catch(DataAccessException dae){
			throw new ServiceException("fail query instance", dae);
		}

	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public List<StorageInfo> getStorages(ServiceContext svcctx, String storagename, String[] types, String[] states) throws ServiceException {

		List<StorageInfo> rtv = null;
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_storages WHERE 1=1 ");
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(storagename)){
			
			SQL.append( " AND storage_name LIKE :storageName ");
			params.put("storageName", "%" + storagename + "%");
		}
		// user type condition
		if(!ArrayUtils.isEmpty(types)){
			SQL.append(" AND storage_type in (:types) "); 
			params.put("types", Arrays.asList(types));
		}
		// user state condition
		if(!ArrayUtils.isEmpty(states)){
			SQL.append(" AND state in (:states) "); 
			params.put("states", Arrays.asList(states));
		}
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);

		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		try{
			rtv = jtemplate.query(SQL.toString(), params, storagedao.getRowMapper());	
		}catch(DataAccessException dae){
			throw new ServiceException("fail query instance", dae);
		}

		return rtv;
	}
	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public PageWrapper<StorageInfo> getStorages(ServiceContext svcctx, String storagename, PageQuery pagequery) throws ServiceException {

		List<StorageInfo> rtv = null;
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_storages WHERE 1=1 ");
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(storagename)){
			
			SQL.append( " AND storage_name LIKE :storageName ");
			params.put("storageName", "%" + storagename + "%");
		}
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		PageWrapper<StorageInfo> pwrapper = new PageWrapper<StorageInfo>();
		// get count sql scripts.
		String countsql = StringUtils.replace(SQL.toString(), "SELECT * FROM", "SELECT count(1) FROM");
		int totalrow = pseudodao.queryRowCount(jtemplate, countsql, params);
		// calculate pagination information, the page menu number is 5
		PaginationInfo pagination = new PaginationHelper(totalrow, 
				pagequery.getPageNumber(), 
				pagequery.getPageSize(), 5).getPaginationInfo();
		pwrapper.setPagination(pagination);
		
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL.toString(), pagequery);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + pagesql + " / params : " + ArrayUtils.toString(params));
		}
		try{
			rtv = jtemplate.query(pagesql, params, storagedao.getRowMapper());	
		}catch(DataAccessException dae){
			throw new ServiceException("fail query instance", dae);
		}
		pwrapper.setRows(rtv);
		
		return pwrapper;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public void updateStorage(ServiceContext svcctx, StorageInfo storage) throws ServiceException {
		
		try{
			svcctx.setTraceInfo(storage);
			storagedao.update(storage);
		}catch(DataAccessException dae){
			throw new ServiceException("fail update storage", dae);
		}
	}
}
