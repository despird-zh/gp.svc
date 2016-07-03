package com.gp.svc.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.Cabinets;
import com.gp.common.FlatColumns;
import com.gp.common.GeneralConstants;
import com.gp.common.GroupUsers;
import com.gp.common.IdKey;
import com.gp.common.SystemOptions;
import com.gp.common.ServiceContext;
import com.gp.common.GroupUsers.UserState;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.CabinetDAO;
import com.gp.dao.GroupUserDAO;
import com.gp.dao.PseudoDAO;
import com.gp.dao.UserDAO;
import com.gp.dao.impl.DAOSupport;
import com.gp.dao.impl.UserDAOImpl;
import com.gp.exception.ServiceException;
import com.gp.info.CabinetInfo;
import com.gp.info.CombineInfo;
import com.gp.info.GroupUserInfo;
import com.gp.info.InfoId;
import com.gp.info.KVPair;
import com.gp.info.SysOptionInfo;
import com.gp.info.UserInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.pagination.PaginationHelper;
import com.gp.pagination.PaginationInfo;
import com.gp.svc.CommonService;
import com.gp.svc.SecurityService;
import com.gp.svc.SystemService;
import com.gp.svc.info.UserExt;
import com.gp.util.DateTimeUtils;

/**
 * Security service covers all the operation related with security,
 * include group, user etc. operation. 
 **/
@Service("securityService")
public class SecurityServiceImpl implements SecurityService{
	
	public static Logger LOGGER = LoggerFactory.getLogger(SecurityServiceImpl.class);
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Autowired
	private UserDAO userdao;
	
	@Autowired
	private CabinetDAO cabinetdao;
	
	@Autowired
	private GroupUserDAO groupuserdao;
	
	@Autowired
	CommonService idService;
	
	@Autowired 
	SystemService systemservice;
	
	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean newAccount(ServiceContext svcctx, UserInfo uinfo, Long pubcapacity, Long pricapacity) throws ServiceException {
			
		svcctx.setTraceInfo(uinfo);
		uinfo.setCreateDate(DateTimeUtils.now());		
		// allocate an key for public cabinet
		InfoId<Long> pubkey = idService.generateId( IdKey.CABINET, Long.class);
		uinfo.setPublishCabinet(pubkey.getId());

		CabinetInfo pubinfo = new CabinetInfo();
		pubinfo.setInfoId(pubkey);
		pubinfo.setSourceId(GeneralConstants.LOCAL_INSTANCE);
		pubinfo.setWorkgroupId(GeneralConstants.PERSON_WORKGROUP);
		pubinfo.setCabinetName(uinfo.getAccount());
		pubinfo.setCabinetType(Cabinets.CabinetType.PUBLISH.name());
		pubinfo.setDescription(uinfo.getFullName() + "'s cabinet");			
		pubinfo.setCreateDate(DateTimeUtils.now());
		pubinfo.setCreator(svcctx.getPrincipal().getAccount());
		pubinfo.setModifier(svcctx.getPrincipal().getAccount());
		pubinfo.setModifyDate(DateTimeUtils.now());
		pubinfo.setStorageId(uinfo.getStorageId());

		if(null == pubcapacity){
			// set capacity 
			SysOptionInfo sysopt = systemservice.getOption(svcctx, SystemOptions.PERSONAL_CABINET_QUOTA.toString());
			pubcapacity = StringUtils.isNumeric(sysopt.getOptionValue()) ? Long.valueOf(sysopt.getOptionValue()) :512L;
		}
		pubinfo.setCapacity(pubcapacity);

		// allocate an key for private cabinet
		InfoId<Long> prikey = idService.generateId( IdKey.CABINET, Long.class);
		uinfo.setNetdiskCabinet(prikey.getId());
		
		CabinetInfo priinfo = new CabinetInfo();
		priinfo.setInfoId(prikey);
		priinfo.setSourceId(GeneralConstants.LOCAL_INSTANCE);
		priinfo.setWorkgroupId(GeneralConstants.PERSON_WORKGROUP);
		priinfo.setCabinetName(uinfo.getAccount());
		priinfo.setCabinetType(Cabinets.CabinetType.NETDISK.name());
		priinfo.setDescription(uinfo.getFullName() + "'s cabinet");			
		priinfo.setCreateDate(DateTimeUtils.now());
		priinfo.setCreator(svcctx.getPrincipal().getAccount());
		priinfo.setModifier(svcctx.getPrincipal().getAccount());
		priinfo.setModifyDate(DateTimeUtils.now());
		priinfo.setStorageId(uinfo.getStorageId());
		
		if(null == pricapacity){
			// set capacity 
			SysOptionInfo sysopt = systemservice.getOption(svcctx, SystemOptions.PERSONAL_CABINET_QUOTA.toString());
			pricapacity = StringUtils.isNumeric(sysopt.getOptionValue()) ? Long.valueOf(sysopt.getOptionValue()) :512L;
		}
		// set capacity 
		priinfo.setCapacity(pricapacity);
		int cnt = 0;
		try{			
			cnt = userdao.create( uinfo);		
			cabinetdao.create( pubinfo);
			cabinetdao.create( priinfo);
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.create", dae, "account");
		}
		return cnt > 0;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public int updateAccount(ServiceContext svcctx, UserInfo uinfo, Long pubcapacity, Long pricapacity) throws ServiceException {

		svcctx.setTraceInfo(uinfo);
		uinfo.setCreateDate(DateTimeUtils.now());		
		
		try{
			UserInfo oldinfo = userdao.query(uinfo.getInfoId());
			
			if(StringUtils.isBlank(uinfo.getPassword()))
				uinfo.setPassword(oldinfo.getPassword());
			
			InfoId<Integer> storageId = IdKey.STORAGE.getInfoId(uinfo.getStorageId());
			
			InfoId<Long> pubcab = null;
			if(null != pubcapacity){
				pubcab = IdKey.CABINET.getInfoId(oldinfo.getPublishCabinet());
				cabinetdao.updateCabCapacity(pubcab, pubcapacity);
			}
			cabinetdao.changeStorage(pubcab, storageId);
			
			InfoId<Long> pricab = null;
			if(null != pricapacity){
				pricab = IdKey.CABINET.getInfoId(oldinfo.getNetdiskCabinet());
				cabinetdao.updateCabCapacity(pricab, pricapacity);
			}
			cabinetdao.changeStorage(pricab, storageId);
			
			int cnt = userdao.updateAsNeed(uinfo);		
			
			return cnt ;
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.update", dae, "account");
		}

	}
	
	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean newAccountExt(ServiceContext svcctx, UserInfo uinfo) throws ServiceException {

		svcctx.setTraceInfo(uinfo);
		uinfo.setCreateDate(DateTimeUtils.now());		
		int cnt = 0;
		try{

			cnt = userdao.create( uinfo);		

		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.create", dae, "external account");
		}
		return cnt > 0;
	}
	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public UserInfo getAccountLite(ServiceContext svcctx, InfoId<Long> userId, String account, String type) throws ServiceException {
		
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
			throw new ServiceException("excp.query", dae, "Account lite information");
		}
		return uinfo;
		
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public String getAccountRole(ServiceContext svcctx, InfoId<Long> wgroupId, String account)
			throws ServiceException {
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_group_user WHERE group_id =? AND account = ?");
		String role = null;
		try{
			Long mbrgid = pseudodao.query(wgroupId, FlatColumns.MBR_GRP_ID, Long.class);
			if(null == mbrgid || mbrgid == 0){
				throw new ServiceException("excp.invld.id", wgroupId);
			}
			
			Object[] params = new Object[]{
					mbrgid, account
			};
			
			if(LOGGER.isDebugEnabled())
				LOGGER.debug("SQL : {} / PARAMS : {}", SQL.toString(), ArrayUtils.toString(params));
			
			JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
			List<GroupUserInfo> gulist = jtemplate.query(SQL.toString(), params, groupuserdao.getRowMapper());
			role = CollectionUtils.isEmpty(gulist)? null : gulist.get(0).getRole();
		
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query", dae, "Account lite information");
		}
		return role;
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public Set<KVPair<Long, String>> getAccountGroups(ServiceContext svcctx, InfoId<Long> wgroupId, String account)
			throws ServiceException {
		
		final Set<KVPair<Long, String>> grpset = new HashSet<KVPair<Long, String>>();
		StringBuffer SQL = new StringBuffer();

		SQL.append("SELECT grps.group_id, grps.group_name ");
		SQL.append("FROM gp_groups grps, gp_group_user gusr ");
		SQL.append("WHERE ");
		SQL.append("  grps.workgroup_id = gusr.workgroup_id AND ");
		SQL.append("  grps.group_id = gusr.group_id AND grps.group_type = '").append(GroupUsers.GroupType.WORKGROUP_GRP.name()).append("' AND ");
		SQL.append("  gusr.workgroup_id = ? AND ");
		SQL.append("  gusr.account = ? ");
		
		Object[] params = new Object[]{
			wgroupId.getId(), account
		};
		
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL.toString(), ArrayUtils.toString(params));
		
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		try{
			jtemplate.query(SQL.toString(), params, new RowCallbackHandler(){
	
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					KVPair<Long, String> kvp = new KVPair<Long, String>();
					kvp.setKey(rs.getLong("group_id"));
					kvp.setValue(rs.getString("group_name"));
					grpset.add(kvp);
				}});
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae,"workgroup groups", account + "/" + wgroupId);
		}
		return grpset;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean removeAccount(ServiceContext svcctx, InfoId<Long> userId, String account) throws ServiceException {
		
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
			throw new ServiceException("excp.remove.acnt", dae, account);
		}
		return cnt > 0;		
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean changePassword(ServiceContext svcctx, String account, String password) throws ServiceException {
		int cnt = -1;
		try{
			cnt = userdao.changePassword( account, password);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.update.with", dae, account, "new password");
		}
		return cnt > 0;
		
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public boolean existAccount(ServiceContext svcctx, String account) throws ServiceException {
		try{
			return userdao.existAccount( account);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "account existence", account);
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public CombineInfo<UserInfo, UserExt> getAccountFull(ServiceContext svcctx, InfoId<Long> userId, String account, String type)
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
		List<CombineInfo<UserInfo, UserExt>> users = null;
		try{
			
			users = jtemplate.query(querysql, params, UserExMapper);
			
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "account full informaiton", account);
		}

		return CollectionUtils.isEmpty(users)? null : users.get(0);
	}	
	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public List<CombineInfo<UserInfo, UserExt>> getAccounts(ServiceContext svcctx, String accountname, Integer instanceId, String[] types,String[] states)
			throws ServiceException {
		
		List<CombineInfo<UserInfo, UserExt>> rtv = null;
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
			throw new ServiceException("excp.query", dae, "accounts");
		}

		return rtv;
	}
	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public PageWrapper<CombineInfo<UserInfo, UserExt>> getAccounts(ServiceContext svcctx, String accountname, Integer instanceId, String[] type, PageQuery pagequery)
			throws ServiceException {
		
		List<CombineInfo<UserInfo, UserExt>> rtv = null;
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
		
		PageWrapper<CombineInfo<UserInfo, UserExt>> pwrapper = new PageWrapper<CombineInfo<UserInfo, UserExt>>();
		if(pagequery.isTotalCountEnable()){
			// get count sql scripts.
			String countsql = SQL_COUNT.append(SQL_FROM.toString()).toString();
			int totalrow = pseudodao.queryRowCount(jtemplate, countsql, params);
			// calculate pagination information, the page menu number is 5
			PaginationInfo pagination = new PaginationHelper(totalrow, 
					pagequery.getPageNumber(), 
					pagequery.getPageSize(), 5).getPaginationInfo();
			
			pwrapper.setPagination(pagination);
		}
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL_COLS.append(SQL_FROM.toString()).toString(), pagequery);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + pagesql + " / params : " + ArrayUtils.toString(params));
		}
		try{
			rtv = jtemplate.query(pagesql, params, UserExMapper);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query", dae, "accounts");
		}
		pwrapper.setRows(rtv);
		
		return pwrapper;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean updateLogonTrace(ServiceContext svcctx ,InfoId<Long> userkey, boolean resetRetry) throws ServiceException {
		
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
			throw new ServiceException("excp.update", dae, "logon trace");
		}
		return cnt > 0;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean changeAccountState(ServiceContext svcctx, InfoId<Long> userkey, UserState state) throws ServiceException {
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
			throw new ServiceException("excp.update.with", dae ,"account's state", userkey);
		}
		return cnt > 0;
	}

	public static RowMapper<CombineInfo<UserInfo, UserExt>> UserExMapper = new RowMapper<CombineInfo<UserInfo, UserExt>>(){

		@Override
		public CombineInfo<UserInfo, UserExt> mapRow(ResultSet rs, int rowNum) throws SQLException {
			CombineInfo<UserInfo, UserExt> cinfo = new CombineInfo<UserInfo, UserExt>();
			UserInfo info = UserDAOImpl.UserMapper.mapRow(rs, rowNum);
			// save extend data
			UserExt ext = new UserExt();
			if(DAOSupport.hasColInResultSet(rs, "storage_name")){
				ext.setStorageName(rs.getString("storage_name"));
			}
			ext.setAbbr(rs.getString("abbr"));
			ext.setShortName(rs.getString("short_name"));
			ext.setInstanceName(rs.getString("instance_name"));
	
			cinfo.setPrimary(info);
			cinfo.setExtended(ext);
			
			return cinfo;
		}};
		
}
