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
import com.gp.common.IdKeys;
import com.gp.common.ServiceContext;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.common.DataSourceHolder;
import com.gp.dao.BinaryDAO;
import com.gp.dao.PseudoDAO;
import com.gp.dao.StorageDAO;
import com.gp.exception.ServiceException;
import com.gp.dao.info.BinaryInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.StorageInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.pagination.PaginationHelper;
import com.gp.pagination.PaginationInfo;
import com.gp.svc.StorageService;
import com.gp.util.StorageUtils;

@Service
public class StorageServiceImpl implements StorageService{

	Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);
	
	@Autowired
	private StorageDAO storagedao;
		
	@Autowired
	private BinaryDAO binarydao;
	
	@Autowired
	private PseudoDAO pseudodao;

	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public boolean newBinary(ServiceContext svcctx, BinaryInfo binary) throws ServiceException {

		try{
			InfoId<Integer> storageId = IdKeys.getInfoId(IdKey.GP_STORAGES,binary.getStorageId());
			String storeLoc = StorageUtils.toURIStr(storageId, binary.getInfoId().getId(), binary.getFormat());
			binary.setStoreLocation(storeLoc);
			svcctx.setTraceInfo(binary);
			return binarydao.create(binary) > 0;
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.create",dae, "Binary");
		}
	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly=true)
	@Override
	public BinaryInfo getBinaryByHash(ServiceContext svcctx, String hashstr) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly=true)
	@Override
	public BinaryInfo getBinary(ServiceContext svcctx, InfoId<Long> id) throws ServiceException {
		try{			
			return binarydao.query(id);
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with",dae, "binary", id);
		}
	}

	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public boolean removeBinary(ServiceContext svcctx, InfoId<Long> id) throws ServiceException {
		
		try{			
			return binarydao.delete(id) > 0;
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.delete.with",dae, "binary", id);
		}

	}

	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public boolean newStorage(ServiceContext svcctx, StorageInfo storage) throws ServiceException {
		try{
			svcctx.setTraceInfo(storage);
			return storagedao.create(storage) > 0;	
		}catch(DataAccessException dae){
			throw new ServiceException("excp.create", dae, "storage");
		}
	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly=true)
	@Override
	public StorageInfo getStorage(ServiceContext svcctx, InfoId<Integer> id) throws ServiceException {
		try{
			return storagedao.query(id);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "storage", id);
		}
	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly=true)
	@Override
	public boolean existStorage(ServiceContext svcctx, String storagename) throws ServiceException {
		String CNT_SQL = "SELECT Count(storage_id) from gp_storages where storage_name = '" + storagename + "'";
		try{
			return pseudodao.queryRowCount(CNT_SQL) > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.row.count", dae);
		}
	}

	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public void updateStorage(ServiceContext svcctx, InfoId<Integer> id, int used) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public boolean removeStorage(ServiceContext svcctx, InfoId<Integer> id) throws ServiceException {
		try{
			int cnt = storagedao.delete(id);
			return cnt >0 ;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.delete.with", dae, "storage", id);
		}

	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly=true)
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
			rtv = jtemplate.query(SQL.toString(), params, StorageDAO.StorageMapper);	
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query", dae, "storage");
		}

		return rtv;
	}
	
	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly=true)
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
			rtv = jtemplate.query(pagesql, params, StorageDAO.StorageMapper);	
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query", dae, "storage");
		}
		pwrapper.setRows(rtv);
		
		return pwrapper;
	}

	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public void updateStorage(ServiceContext svcctx, StorageInfo storage) throws ServiceException {
		
		try{
			svcctx.setTraceInfo(storage);
			storagedao.update(storage,FilterMode.NONE);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.update", dae, "storage");
		}
	}
}
