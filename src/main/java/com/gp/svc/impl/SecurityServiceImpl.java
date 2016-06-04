package com.gp.svc.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.Cabinets;
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.SystemOptions;
import com.gp.common.ServiceContext;
import com.gp.common.Users.UserState;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.CabinetDAO;
import com.gp.dao.PseudoDAO;
import com.gp.dao.UserDAO;
import com.gp.dao.impl.DAOSupport;
import com.gp.exception.ServiceException;
import com.gp.info.CabinetInfo;
import com.gp.info.InfoId;
import com.gp.info.SysOptionInfo;
import com.gp.info.UserExInfo;
import com.gp.info.UserInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.pagination.PaginationHelper;
import com.gp.pagination.PaginationInfo;
import com.gp.svc.CommonService;
import com.gp.svc.SecurityService;
import com.gp.svc.SystemService;
import com.gp.util.DateTimeUtils;

/**
 * Security service covers all the operation related with security,
 * include group, user etc. operation. 
 **/
@Service("securityService")
public class SecurityServiceImpl implements SecurityService{
	
	Logger LOGGER = LoggerFactory.getLogger(SecurityServiceImpl.class);
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Autowired
	private UserDAO userdao;
	
	@Autowired
	private CabinetDAO cabinetdao;
	
	@Autowired
	CommonService idService;
	
	@Autowired 
	SystemService systemservice;
	
	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean newAccount(ServiceContext<?> svcctx, UserInfo uinfo) throws ServiceException {
			
		svcctx.setTraceInfo(uinfo);
		uinfo.setCreateDate(DateTimeUtils.now());		
		// allocate an key for public cabinet
		InfoId<Long> pubkey = idService.generateId( IdKey.CABINET, Long.class);
		uinfo.setPublishCabinet(pubkey.getId());

		CabinetInfo pubinfo = new CabinetInfo();
		pubinfo.setInfoId(pubkey);
		pubinfo.setWorkgroupId(GeneralConstants.PERSON_WORKGROUP);
		pubinfo.setCabinetName(uinfo.getAccount());
		pubinfo.setCabinetType(Cabinets.CabinetType.PUBLISH.name());
		pubinfo.setDescription(uinfo.getFullName() + "'s cabinet");			
		pubinfo.setCreateDate(DateTimeUtils.now());
		pubinfo.setCreator(svcctx.getPrincipal().getAccount());
		pubinfo.setModifier(svcctx.getPrincipal().getAccount());
		pubinfo.setModifyDate(DateTimeUtils.now());
		pubinfo.setStorageId(uinfo.getStorageId());
		Long capacity = null;
		capacity = svcctx.getContextData(CTX_KEY_PUBCAPACITY, Long.class);
		if(null == capacity){
			// set capacity 
			SysOptionInfo sysopt = systemservice.getOption(svcctx, SystemOptions.PERSONAL_CABINET_QUOTA.toString());
			capacity = StringUtils.isNumeric(sysopt.getOptionValue()) ? Long.valueOf(sysopt.getOptionValue()) :512L;
		}
		pubinfo.setCapacity(capacity);

		// allocate an key for private cabinet
		InfoId<Long> prikey = idService.generateId( IdKey.CABINET, Long.class);
		uinfo.setNetdiskCabinet(prikey.getId());
		
		CabinetInfo priinfo = new CabinetInfo();
		priinfo.setInfoId(prikey);
		priinfo.setWorkgroupId(GeneralConstants.PERSON_WORKGROUP);
		priinfo.setCabinetName(uinfo.getAccount());
		priinfo.setCabinetType(Cabinets.CabinetType.NETDISK.name());
		priinfo.setDescription(uinfo.getFullName() + "'s cabinet");			
		priinfo.setCreateDate(DateTimeUtils.now());
		priinfo.setCreator(svcctx.getPrincipal().getAccount());
		priinfo.setModifier(svcctx.getPrincipal().getAccount());
		priinfo.setModifyDate(DateTimeUtils.now());
		priinfo.setStorageId(uinfo.getStorageId());
		
		Long pcapacity = null;
		pcapacity = svcctx.getContextData(CTX_KEY_PRICAPACITY, Long.class);
		if(null == pcapacity){
			// set capacity 
			SysOptionInfo sysopt = systemservice.getOption(svcctx, SystemOptions.PERSONAL_CABINET_QUOTA.toString());
			pcapacity = StringUtils.isNumeric(sysopt.getOptionValue()) ? Long.valueOf(sysopt.getOptionValue()) :512L;
		}
		// set capacity 
		priinfo.setCapacity(pcapacity);
		int cnt = 0;
		try{			
			cnt = userdao.create( uinfo);		
			cabinetdao.create( pubinfo);
			cabinetdao.create( priinfo);
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail create user account", dae);
		}
		return cnt > 0;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public int updateAccount(ServiceContext<?> svcctx, UserInfo uinfo) throws ServiceException {

		svcctx.setTraceInfo(uinfo);
		uinfo.setCreateDate(DateTimeUtils.now());		
		
		try{
			UserInfo oldinfo = userdao.query(uinfo.getInfoId());
			
			if(StringUtils.isBlank(uinfo.getPassword()))
				uinfo.setPassword(oldinfo.getPassword());
			
			InfoId<Integer> storageId = IdKey.STORAGE.getInfoId(uinfo.getStorageId());
			Long pubcapacity = svcctx.getContextData(CTX_KEY_PUBCAPACITY, Long.class);
			InfoId<Long> pubcab = null;
			if(null != pubcapacity){
				pubcab = IdKey.CABINET.getInfoId(oldinfo.getPublishCabinet());
				cabinetdao.updateCabCapacity(pubcab, pubcapacity);
			}
			cabinetdao.changeStorage(pubcab, storageId);
			Long pricapacity = svcctx.getContextData(CTX_KEY_PRICAPACITY, Long.class);
			InfoId<Long> pricab = null;
			if(null != pricapacity){
				pricab = IdKey.CABINET.getInfoId(oldinfo.getNetdiskCabinet());
				cabinetdao.updateCabCapacity(pricab, pricapacity);
			}
			cabinetdao.changeStorage(pricab, storageId);
			
			int cnt = userdao.updateAsNeed(uinfo);		
			
			return cnt ;
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail create user account", dae);
		}

	}
	
	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean newAccountExt(ServiceContext<?> svcctx, UserInfo uinfo) throws ServiceException {

		svcctx.setTraceInfo(uinfo);
		uinfo.setCreateDate(DateTimeUtils.now());		
		int cnt = 0;
		try{

			cnt = userdao.create( uinfo);		

		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail create external account", dae);
		}
		return cnt > 0;
	}
	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public UserInfo getAccountLite(ServiceContext<?> svcctx, InfoId<Long> userId, String account, String type) throws ServiceException {
		
		UserInfo uinfo = null;
		try{
			if(userId != null && InfoId.isValid(userId)){
			
				uinfo = userdao.query(userId);
			}else if(StringUtils.isBlank(type)){
			
				uinfo = userdao.queryByAccount( account);
			}else{
				
				StringBuffer SQL = new StringBuffer("Select * from gp_users where account = ? and type = ?");
				Object[] parms = new Object[]{account, type};
				
				JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
				
				uinfo = jtemplate.queryForObject(SQL.toString(), parms, userdao.getRowMapper());
				
			}
		}catch(DataAccessException dae){
			throw new ServiceException("Fail get user account", dae);
		}
		return uinfo;
		
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public Set<String> getAccountRoles(ServiceContext<?> svcctx, InfoId<Long> wkey, String account)
			throws ServiceException {
		
		return null;
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public Set<String> getAccountGroups(ServiceContext<?> svcctx, InfoId<Long> wkey, String account)
			throws ServiceException {
		
		return null;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean removeAccount(ServiceContext<?> svcctx, InfoId<Long> userId, String account) throws ServiceException {
		
		int cnt = -1;
		try{
			UserInfo info = null;
			if(null != userId && InfoId.isValid(userId)){
				info = userdao.query(userId);
			}else{
				info = userdao.queryByAccount( account);
			}
			InfoId<Long> uid = info.getInfoId();
			svcctx.setAuditObject(uid);
	
			cnt = userdao.delete( info.getInfoId());			
			// get personal public cabinet id.
			Long cid = info.getPublishCabinet();			
			// create cabinet id
			InfoId<Long> cabId = IdKey.CABINET.getInfoId( cid);			
			// delete cabinet 
			cabinetdao.delete( cabId);
			// get personal private cabinet id.
			cid = info.getNetdiskCabinet();			
			// create cabinet id
			cabId = IdKey.CABINET.getInfoId( cid);			
			// delete cabinet 
			cabinetdao.delete( cabId);
			
		}catch(DataAccessException dae){
			throw new ServiceException("Fail remove user account", dae);
		}
		return cnt > 0;		
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean changePassword(ServiceContext<?> svcctx, String account, String password) throws ServiceException {
		int cnt = -1;
		try{
			cnt = userdao.changePassword( account, password);
		}catch(DataAccessException dae){
			throw new ServiceException("Fail change password", dae);
		}
		return cnt > 0;
		
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public boolean existAccount(ServiceContext<?> svcctx, String account) throws ServiceException {
		
		boolean exist = userdao.existAccount( account);

		return exist;
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public UserExInfo getAccountFull(ServiceContext<?> svcctx, InfoId<Long> userId, String account, String type)
			throws ServiceException {

		StringBuffer SQL_COLS = new StringBuffer("SELECT a.*,b.*,c.* ");
		StringBuffer SQL_FROM = new StringBuffer("FROM gp_users a ")
				.append("LEFT JOIN ( SELECT storage_id, storage_name FROM gp_storages) c ")
				.append("ON a.storage_id = c.storage_id ")
				.append("LEFT JOIN ( SELECT instance_id, instance_name,short_name, abbr FROM gp_instances) b ")
				.append("ON a.source_id = b.instance_id WHERE 1 = 1 ");
		Map<String,Object> params = new HashMap<String,Object>();
		// account or name condition
		if(StringUtils.isNotBlank(account)){
			
			SQL_FROM.append(" AND a.account = :account ");
			params.put("account", StringUtils.trim(account));
		}
		// entity condition
		if(null != userId && InfoId.isValid(userId)){
			
			SQL_FROM.append(" AND a.user_id = :userid ");
			params.put("userid", userId.getId());
		}
		// user type condition
		if(StringUtils.isNotBlank(type)){
			SQL_FROM.append(" AND a.type = :type"); 
			params.put("type", StringUtils.trim(type));
		}
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		String querysql = SQL_COLS.append(SQL_FROM).toString();
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + querysql + " / params : " + ArrayUtils.toString(params));
		}
		List<UserExInfo> users = null;
		try{
			
			users = jtemplate.query(querysql, params, UserExMapper);
			
		}catch(DataAccessException dae){
			throw new ServiceException("Fail query accounts", dae);
		}

		return CollectionUtils.isEmpty(users)? null : users.get(0);
	}	
	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public List<UserExInfo> getAccounts(ServiceContext<?> svcctx, String accountname, Integer instanceId, String[] types,String[] states)
			throws ServiceException {
		
		List<UserExInfo> rtv = null;
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.*,b.*,c.* ");
		StringBuffer SQL_FROM = new StringBuffer("FROM gp_users a ")
				.append("LEFT JOIN ( SELECT storage_id, storage_name FROM gp_storages) c ")
				.append("ON a.storage_id = c.storage_id ")
				.append("LEFT JOIN ( SELECT instance_id, instance_name,short_name, abbr FROM gp_instances) b ")
				.append("ON a.source_id = b.instance_id WHERE 1 = 1 ");
		Map<String,Object> params = new HashMap<String,Object>();
		// account or name condition
		if(StringUtils.isNotBlank(accountname)){
			
			SQL_FROM.append(" AND (a.account like :uname or a.full_name like :fname ) ");
			params.put("uname", "%" + StringUtils.trim(accountname) + "%");
			params.put("fname", "%" + StringUtils.trim(accountname) + "%");
		}
		// entity condition
		if(null != instanceId){
			
			SQL_FROM.append(" AND a.source_id = :srcid ");
			params.put("srcid", instanceId);
		}
		// user type condition
		if(!ArrayUtils.isEmpty(types)){
			SQL_FROM.append(" AND a.type in ( :types)"); 
			params.put("types", Arrays.asList(types));
		}
		// user state condition
		if(!ArrayUtils.isEmpty(states)){
			SQL_FROM.append(" AND a.state in ( :states)"); 
			params.put("states", Arrays.asList(states));
		}
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		String querysql = SQL_COLS.append(SQL_FROM).toString();
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + querysql + " / params : " + ArrayUtils.toString(params));
		}
		try{
			rtv = jtemplate.query(querysql, params, UserExMapper);
		}catch(DataAccessException dae){
			throw new ServiceException("Fail query accounts", dae);
		}

		return rtv;
	}
	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public PageWrapper<UserExInfo> getAccounts(ServiceContext<?> svcctx, String accountname, Integer instanceId, String[] type, PageQuery pagequery)
			throws ServiceException {
		
		List<UserExInfo> rtv = null;
		StringBuffer SQL_COUNT = new StringBuffer("SELECT count(a.user_id) ");
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.*,b.* ");
		StringBuffer SQL_FROM = new StringBuffer("FROM gp_users a ")
				.append("LEFT JOIN ( SELECT instance_id, instance_name,short_name, abbr FROM gp_instances) b ")
				.append("ON a.source_id = b.instance_id WHERE 1 = 1 ");
		Map<String,Object> params = new HashMap<String,Object>();
		// account or name condition
		if(StringUtils.isNotBlank(accountname)){
			
			SQL_FROM.append(" AND (a.account like :uname or a.full_name like :fname ) ");
			params.put("uname", "%" + StringUtils.trim(accountname) + "%");
			params.put("fname", "%" + StringUtils.trim(accountname) + "%");
		}
		// entity condition
		if(null != instanceId){
			
			SQL_FROM.append(" AND a.source_id = :srcid ");
			params.put("srcid", instanceId);
		}
		// user type condition
		if(!ArrayUtils.isEmpty(type)){
			SQL_FROM.append(" and a.type in ( :types)"); 
			params.put("types", Arrays.asList(type));
		}

		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		PageWrapper<UserExInfo> pwrapper = new PageWrapper<UserExInfo>();
		// get count sql scripts.
		String countsql = SQL_COUNT.append(SQL_FROM.toString()).toString();
		int totalrow = pseudodao.queryRowCount(jtemplate, countsql, params);
		// calculate pagination information, the page menu number is 5
		PaginationInfo pagination = new PaginationHelper(totalrow, 
				pagequery.getPageNumber(), 
				pagequery.getPageSize(), 5).getPaginationInfo();
		
		pwrapper.setPagination(pagination);
		
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL_COLS.append(SQL_FROM.toString()).toString(), pagequery);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + pagesql + " / params : " + ArrayUtils.toString(params));
		}
		try{
			rtv = jtemplate.query(pagesql, params, UserExMapper);
		}catch(DataAccessException dae){
			throw new ServiceException("Fail query accounts", dae);
		}
		pwrapper.setRows(rtv);
		
		return pwrapper;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean updateLogonTrace(ServiceContext<?> svcctx ,InfoId<Long> userkey, boolean resetRetry) throws ServiceException {
		
		StringBuffer SQL = new StringBuffer();

		if(resetRetry){
			SQL.append("UPDATE gp_users SET retry_times = 0, last_logon = ? ")
				.append("WHERE user_id = ? ");			
		}else{
			SQL.append("UPDATE gp_users SET retry_times = retry_times + 1, last_logon = ? ")
			.append("WHERE user_id = ? ");
		}
		Object[] params = new Object[]{				
				DateTimeUtils.now(),
				userkey.getId()
		};
		
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int cnt = -1;
		try{
			cnt = jtemplate.update(SQL.toString(), params);
		}catch(DataAccessException dae){
			throw new ServiceException("Fail update logon trace", dae);
		}
		return cnt > 0;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean changeAccountState(ServiceContext<?> svcctx, InfoId<Long> userkey, UserState state) throws ServiceException {
		StringBuffer SQL = new StringBuffer();
		Object[] params = null;
		if(UserState.ACTIVE == state){
			
			SQL.append("UPDATE gp_users SET retry_times = 0, state = ? ")
			.append("WHERE user_id = ?");
			
			params = new Object[]{				
					state.name(),
					userkey.getId()
			};
		}else if(UserState.DEACTIVE == state){
			
			SQL.append("UPDATE gp_users SET state = ? ")
			.append("WHERE user_id = ? ");
			params = new Object[]{				
					state.name(),
					userkey.getId()
			};
		}else{
			
			SQL.append("UPDATE gp_users SET last_logon = ? , state = ? ")
			.append("WHERE user_id = ? ");
			
			params = new Object[]{	
					DateTimeUtils.now(),
					state.name(),
					userkey.getId()
			};
		}
		
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int cnt = -1;
		try{
			cnt = jtemplate.update(SQL.toString(), params);
		}catch(DataAccessException dae){
			throw new ServiceException("Fail update account state", dae);
		}
		return cnt > 0;
	}

	
	public static RowMapper<UserExInfo> UserExMapper = new RowMapper<UserExInfo>(){

		@Override
		public UserExInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserExInfo info = new UserExInfo();
			InfoId<Long> id = IdKey.USER.getInfoId(rs.getLong("user_id"));
			info.setInfoId(id);

			info.setSourceId(rs.getInt("source_id"));
			info.setAccount(rs.getString("account"));
			info.setType(rs.getString("type"));
			info.setMobile(rs.getString("mobile"));
			info.setPhone(rs.getString("phone"));
			info.setFullName(rs.getString("full_name"));
			info.setEmail(rs.getString("email"));
			info.setPassword(rs.getString("password"));
			info.setState(rs.getString("state"));
			info.setCreateDate(rs.getTimestamp("create_time"));
			info.setExtraInfo(rs.getString("extra_info"));
			info.setRetryTimes(rs.getInt("retry_times"));
			info.setLastLogonDate(rs.getDate("last_logon"));
			info.setLanguage(rs.getString("language"));
			info.setTimeZone(rs.getString("timezone"));
			info.setPublishCabinet(rs.getLong("publish_cabinet_id"));
			info.setNetdiskCabinet(rs.getLong("netdisk_cabinet_id"));
			info.setGlobalAccount(rs.getString("global_account"));
			info.setStorageId(rs.getInt("storage_id"));
			if(DAOSupport.hasColInResultSet(rs, "storage_name")){
				info.setStorageName(rs.getString("storage_name"));
			}
			info.setAbbr(rs.getString("abbr"));
			info.setShortName(rs.getString("short_name"));
			info.setInstanceName(rs.getString("instance_name"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}};
		
	@Override
	public List<UserInfo> getAccounts(ServiceContext<?> svcctx, List<String> accounts) throws ServiceException {
		
		
		return null;
	}
	
}
