package com.gp.svc.impl;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
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
import com.gp.common.Images;
import com.gp.common.SystemOptions;
import com.gp.common.ServiceContext;
import com.gp.dao.CabinetDAO;
import com.gp.dao.GroupDAO;
import com.gp.dao.GroupUserDAO;
import com.gp.dao.ImageDAO;
import com.gp.dao.PseudoDAO;
import com.gp.dao.UserDAO;
import com.gp.dao.WorkgroupDAO;
import com.gp.dao.WorkgroupUserDAO;
import com.gp.exception.ServiceException;
import com.gp.info.CabinetInfo;
import com.gp.info.GroupInfo;
import com.gp.info.GroupUserInfo;
import com.gp.info.ImageInfo;
import com.gp.info.InfoId;
import com.gp.info.SysOptionInfo;
import com.gp.info.UserExInfo;
import com.gp.info.UserInfo;
import com.gp.info.WorkgroupExInfo;
import com.gp.info.WorkgroupInfo;
import com.gp.info.WorkgroupUserExInfo;
import com.gp.info.WorkgroupUserInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.pagination.PaginationHelper;
import com.gp.pagination.PaginationInfo;
import com.gp.svc.IdService;
import com.gp.svc.SystemService;
import com.gp.svc.WorkgroupService;
import com.gp.util.DateTimeUtils;

/**
 * define operation on workgroup 
 * 
 * @author gary diao
 * @version 0.1 2014-12-12
 **/
@Service("workgroupService")
public class WorkgroupServiceImpl implements WorkgroupService{

	private static Logger LOGGER = LoggerFactory.getLogger(WorkgroupServiceImpl.class);
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Autowired
	WorkgroupDAO workgroupdao;
	
	@Autowired
	CabinetDAO cabinetdao;

	@Autowired
	UserDAO userdao;

	@Autowired
	GroupDAO groupdao;
	
	@Autowired
	GroupUserDAO groupuserdao;
	
	@Autowired
	WorkgroupUserDAO workgroupuserdao;
	
	@Autowired
	IdService idService;
	
	@Autowired 
	SystemService systemservice;
	
	@Autowired
	ImageDAO imagedao;
	
	/**
	 * When create new workgroup, the public capacity and private capacity is stored in service context.
	 * the parameters in context includes : _pub_capacity / _pri_capacity / _image_path
	 * 
	 * @param svcctx The service context object
	 * @param winfo The workgroup information object
	 * 
	 * @return the info id object
	 * 
	 **/
	@Transactional
	@Override
	public boolean newWorkgroup(ServiceContext<?> svcctx, WorkgroupInfo winfo) throws ServiceException {
		
		InfoId<Long> wkey = winfo.getInfoId();
		// set trace info
		svcctx.setTraceInfo(winfo);
		// allocate an key for public cabinet
		InfoId<Long> pubkey = idService.generateId( IdKey.CABINET, Long.class);
		winfo.setPublishCabinet(pubkey.getId());
		SysOptionInfo veropt= systemservice.getOption(svcctx, SystemOptions.CABINET_VERSION_ENABLE);
		boolean versionable = Boolean.valueOf(veropt.getOptionValue());
		
		CabinetInfo pubinfo = new CabinetInfo();
		pubinfo.setInfoId(pubkey);
		pubinfo.setWorkgroupId(wkey.getId());
		pubinfo.setCabinetName(winfo.getWorkgroupName());
		pubinfo.setCabinetType(Cabinets.CabinetType.PUBLISH.name());
		pubinfo.setDescription(winfo.getWorkgroupName() + "'s cabinet");			
		pubinfo.setCreateDate(DateTimeUtils.now());
		pubinfo.setCreator(svcctx.getPrincipal().getAccount());
		pubinfo.setModifier(svcctx.getPrincipal().getAccount());
		pubinfo.setModifyDate(DateTimeUtils.now());
		pubinfo.setStorageId(winfo.getStorageId());
		pubinfo.setVersionable(versionable);
		Long capacity = null;
		capacity = svcctx.getContextData(CTX_KEY_PUBCAPACITY, Long.class);
		if(null == capacity){
			// set capacity 
			SysOptionInfo sysopt = systemservice.getOption(svcctx, SystemOptions.WORKGROUP_CABINET_QUOTA);
			capacity = StringUtils.isNumeric(sysopt.getOptionValue()) ? Long.valueOf(sysopt.getOptionValue()) :512L;
			capacity = capacity * 1024 * 1024;// into bytes
		}
		pubinfo.setCapacity(capacity);

		// allocate an key for private cabinet
		InfoId<Long> prikey = idService.generateId( IdKey.CABINET, Long.class);
		winfo.setNetdiskCabinet(prikey.getId());
		
		CabinetInfo priinfo = new CabinetInfo();
		priinfo.setInfoId(prikey);
		priinfo.setWorkgroupId(wkey.getId());
		priinfo.setCabinetName(winfo.getWorkgroupName());
		priinfo.setCabinetType(Cabinets.CabinetType.NETDISK.name());
		priinfo.setDescription(winfo.getWorkgroupName() + "'s cabinet");			
		priinfo.setCreateDate(DateTimeUtils.now());
		priinfo.setCreator(svcctx.getPrincipal().getAccount());
		priinfo.setModifier(svcctx.getPrincipal().getAccount());
		priinfo.setModifyDate(DateTimeUtils.now());
		priinfo.setStorageId(winfo.getStorageId());
		priinfo.setVersionable(versionable);
		Long pcapacity = null;
		pcapacity = svcctx.getContextData(CTX_KEY_PRICAPACITY, Long.class);
		if(null == pcapacity){
			// set capacity 
			SysOptionInfo sysopt = systemservice.getOption(svcctx, SystemOptions.WORKGROUP_CABINET_QUOTA);
			pcapacity = StringUtils.isNumeric(sysopt.getOptionValue()) ? Long.valueOf(sysopt.getOptionValue()) :512L;
			pcapacity = pcapacity * 1024 * 1024;
		}

		// set capacity 
		priinfo.setCapacity(pcapacity);
		int cnt = 0;
		try{
			// create image firstly.		
			String imgpath = svcctx.getContextData(CTX_KEY_IMAGE_PATH, String.class);
			String filename = FilenameUtils.getName(imgpath);
			Long imgid = Images.parseImageId(filename);
			ImageInfo imginfo = imagedao.query(IdKey.IMAGE.getInfoId(imgid));
			// check if the image exists
			if(imginfo == null){ // save the image
				Date createDate = Images.parseTouchDate(filename);
				String extension = FilenameUtils.getExtension(filename);
				
				imginfo = new ImageInfo(imgpath.substring(0, imgpath.lastIndexOf(File.separator) + 1));
				imginfo.setTouchTime(createDate);
				imginfo.setInfoId(IdKey.IMAGE.getInfoId( imgid));
				imginfo.setImageFile(new File(imgpath));
				imginfo.setExtension(extension);
				imginfo.setFormat(extension);
				svcctx.setTraceInfo(imginfo);
				imagedao.create(imginfo);
			}
			winfo.setAvatarId(imgid);			
			cnt = workgroupdao.create( winfo);
			cabinetdao.create( pubinfo);
			cabinetdao.create( priinfo);			

			
		}catch(DataAccessException dae){

			throw new ServiceException("error when create new workgroup", dae);
		}
		return cnt > 0;
	}

	/**
	 * Change the workgroup and related cabinet storage quota information.
	 **/
	@Transactional
	@Override
	public boolean updateWorkgroup(ServiceContext<?> svcctx, WorkgroupInfo winfo) throws ServiceException {
		
		InfoId<Long> wkey = winfo.getInfoId();

		int cnt = 0;
		try{
			// set trace info
			svcctx.setTraceInfo(winfo);

			// create image firstly.		
			String imgpath = svcctx.getContextData(CTX_KEY_IMAGE_PATH, String.class);
			String filename = FilenameUtils.getName(imgpath);
			Long imgid = Images.parseImageId(filename);
			ImageInfo imginfo = imagedao.query(IdKey.IMAGE.getInfoId(imgid));
			// check if the image exists
			if(imginfo == null){ // save the image
				Date createDate = Images.parseTouchDate(filename);
				String extension = FilenameUtils.getExtension(filename);
				
				imginfo = new ImageInfo(imgpath.substring(0, imgpath.lastIndexOf(File.separator) + 1));
				imginfo.setTouchTime(createDate);
				imginfo.setInfoId(IdKey.IMAGE.getInfoId( imgid));
				imginfo.setImageFile(new File(imgpath));
				imginfo.setExtension(extension);
				imginfo.setFormat(extension);
				svcctx.setTraceInfo(imginfo);
				imagedao.create(imginfo);
			}
			winfo.setAvatarId(imgid);	

			// update storage id
			InfoId<Integer> storageId = IdKey.STORAGE.getInfoId(winfo.getStorageId());
			List<CabinetInfo> cabinets = cabinetdao.queryByWorkgroupId(wkey);
			for(CabinetInfo cab : cabinets){
				// public cabinet
				if(Cabinets.CabinetType.PUBLISH.name().equals(cab.getCabinetType())){
					Long pcapacity = svcctx.getContextData(CTX_KEY_PUBCAPACITY, Long.class);
					if(null != pcapacity){
						cabinetdao.updateCabCapacity(cab.getInfoId(), pcapacity);
					}
					cabinetdao.changeStorage(cab.getInfoId(), storageId);
					winfo.setPublishCabinet(cab.getInfoId().getId());
				}
				if(Cabinets.CabinetType.NETDISK.name().equals(cab.getCabinetType())){
					Long pcapacity = svcctx.getContextData(CTX_KEY_PRICAPACITY, Long.class);
					if(null != pcapacity){
						cabinetdao.updateCabCapacity(cab.getInfoId(), pcapacity);
					}
					cabinetdao.changeStorage(cab.getInfoId(), storageId);
					winfo.setNetdiskCabinet(cab.getInfoId().getId());
				}
			}
			
			// update workgroup 
			cnt = workgroupdao.update( winfo);
			
		}catch(DataAccessException dae){

			throw new ServiceException("error when update workgroup", dae);
		}
		return cnt > 0;
	}
	
	@Override
	public WorkgroupInfo getWorkgroup(ServiceContext<?> svcctx, InfoId<Long> wkey) throws ServiceException {
		
		WorkgroupInfo winfo = null;

		winfo = workgroupdao.query( wkey);
		
		return winfo;

	}

	@Override
	public List<WorkgroupUserExInfo> getWorkgroupMembers(ServiceContext<?> svcctx, InfoId<Long> wkey, 
			String uname , InfoId<Integer> sourceId) throws ServiceException {
	
		Map<String,Object> params = new HashMap<String,Object>();
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT b.*,c.instance_name,c.instance_id,a.full_name,a.type,a.email,a.user_id ");

		StringBuffer SQL_FROM = new StringBuffer("FROM gp_workgroup_user b ");
		
		SQL_FROM.append("LEFT JOIN (SELECT user_id,source_id,account,email,full_name,type FROM gp_users) a ")
				.append("  ON b.account = a.account ")
				.append("LEFT JOIN (select instance_name, instance_id FROM gp_instances) c ")
				.append("  ON a.source_id = c.instance_id ")
				.append("WHERE b.workgroup_id = :wgroup_id ");
		
		params.put("wgroup_id", wkey.getId());
		
		if(StringUtils.isNotBlank(uname)){
			
			SQL_FROM.append("AND (a.account like :uname OR a.email like :uname OR a.full_name like :uname) ");
			params.put("uname", "%" + StringUtils.trim(uname) + "%");
		}
		if(InfoId.isValid(sourceId)){
			
			SQL_FROM.append("AND c.instance_id = :source_id ");
			params.put("source_id", sourceId.getId());
		}
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		String querysql = SQL_COLS.append(SQL_FROM).toString();
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + querysql + " / params : " + ArrayUtils.toString(params));
		}
		List<WorkgroupUserExInfo> result = null;
		try{
			result = jtemplate.query(querysql, params, WorkgroupUserExMapper);
		}catch(DataAccessException dae){
			throw new ServiceException("Fail query members", dae);
		}

		return result;
	}

	@Override
	public PageWrapper<WorkgroupUserExInfo> getWorkgroupMembers(ServiceContext<?> svcctx, InfoId<Long> wkey, 
			String uname , InfoId<Integer> sourceId, PageQuery pagequery) throws ServiceException {
	
		Map<String,Object> params = new HashMap<String,Object>();		
		StringBuffer SQL_COLS = new StringBuffer("SELECT b.*,c.instance_name,c.instance_id,a.full_name,a.type,a.email,a.user_id ");
		StringBuffer SQL_COUNT = new StringBuffer("SELECT COUNT(a.user_id) ");
		StringBuffer SQL_FROM = new StringBuffer("FROM gp_workgroup_user b ");
		
		SQL_FROM.append("LEFT JOIN (SELECT user_id,source_id,account,email,full_name,type FROM gp_users) a ")
				.append("  ON b.account = a.account ")
				.append("LEFT JOIN (select instance_name, instance_id FROM gp_instances) c ")
				.append("  ON a.source_id = c.instance_id ")
				.append("WHERE b.workgroup_id = :wgroup_id ");
		
		params.put("wgroup_id", wkey.getId());
		
		if(StringUtils.isNotBlank(uname)){
			
			SQL_FROM.append("AND (a.account like :uname OR a.email like :uname OR a.full_name like :uname) ");
			params.put("uname", "%" + StringUtils.trim(uname) + "%");
		}
		if(InfoId.isValid(sourceId)){
			
			SQL_FROM.append("AND c.instance_id = :source_id ");
			params.put("source_id", sourceId.getId());
		}
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		PageWrapper<WorkgroupUserExInfo> pwrapper = new PageWrapper<WorkgroupUserExInfo>();
		// get count sql scripts.
		String countsql = SQL_COUNT.append(SQL_FROM).toString();
		int totalrow = pseudodao.queryRowCount(jtemplate, countsql, params);
		// calculate pagination information, the page menu number is 5
		PaginationInfo pagination = new PaginationHelper(totalrow, 
				pagequery.getPageNumber(), 
				pagequery.getPageSize(), 5).getPaginationInfo();
		
		pwrapper.setPagination(pagination);
		
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL_COLS.append(SQL_FROM).toString(), pagequery);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + pagesql + " / params : " + ArrayUtils.toString(params));
		}
		List<WorkgroupUserExInfo> result = null;
		try{
			result = jtemplate.query(pagesql, params, WorkgroupUserExMapper);
			pwrapper.setRows(result);
		}catch(DataAccessException dae){
			throw new ServiceException("Fail query members", dae);
		}

		return pwrapper;
	}
	
	@Transactional
	@Override
	public boolean addWorkgroupMember(ServiceContext<?> svcctx, InfoId<Long> wkey, String account, String role)
			throws ServiceException {
		try{
			
			WorkgroupUserInfo wuinfo = workgroupuserdao.queryByAccount(wkey.getId(), account);			
			if(null != wuinfo){
				
				wuinfo.setRole(role);
				svcctx.setTraceInfo(wuinfo);
				workgroupuserdao.update(wuinfo);
			}else{
				// prepare new record
				InfoId<Long> rid = idService.generateId( IdKey.WORKGROUP_USER, Long.class);
				
				wuinfo = new WorkgroupUserInfo();
				wuinfo.setInfoId(rid);
				wuinfo.setAccount(account);
				wuinfo.setRole(role);
				wuinfo.setWorkgroupId(wkey.getId());		
				svcctx.setTraceInfo(wuinfo);					
				workgroupuserdao.create( wuinfo);
			}
		}catch(DataAccessException dae){
			throw new ServiceException("Fail add members", dae);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean removeWorkgroupMember(ServiceContext<?> svcctx, InfoId<Long> wkey, String account)
			throws ServiceException {
		
		StringBuffer DelSQL = new StringBuffer("delete from gp_workgroup_user where workgroup_id = ? and account = ? ");
		
		Object[] params = new Object[]{
				wkey.getId(),
				account
		};
		
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + DelSQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		// remote old records.
		try{
			return jtemplate.update(DelSQL.toString(), params)>0;
		}catch(DataAccessException dae){
			throw new ServiceException("Fail remove members", dae);
		}
	}

	public static RowMapper<WorkgroupUserExInfo> WorkgroupUserExMapper = new RowMapper<WorkgroupUserExInfo>(){

		@Override
		public WorkgroupUserExInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			WorkgroupUserExInfo info = new WorkgroupUserExInfo();
			InfoId<Long> id = IdKey.WORKGROUP_USER.getInfoId(rs.getLong("rel_id"));
			info.setInfoId(id);

			info.setAccount(rs.getString("account"));
			info.setRole(rs.getString("role"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			info.setEmail(rs.getString("email"));
			info.setUserName(rs.getString("full_name"));
			info.setUserType(rs.getString("type"));
			info.setInstanceName(rs.getString("instance_name"));
			InfoId<Long> uid = IdKey.USER.getInfoId(rs.getLong("user_id"));
			
			info.setInstanceId(rs.getInt("instance_id"));
			info.setUserId(uid);
			
			return info;
		}
	};

	@Override
	public List<UserExInfo> getAvailableUsers(ServiceContext<?> svcctx, InfoId<Long> wkey, String uname) throws ServiceException {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.*, b.rel_id, c.* ");

		StringBuffer SQL_FROM = new StringBuffer("FROM gp_users a ");
		SQL_FROM.append("LEFT JOIN (SELECT account,rel_id,workgroup_id from gp_workgroup_user WHERE workgroup_id = :wgroup_id ) b ")
				.append("ON b.account= a.account ")
				.append("LEFT JOIN ( SELECT instance_id, instance_name,short_name, abbr FROM gp_instances) c ")
				.append("ON a.source_id = c.instance_id ")
				.append("WHERE b.rel_id IS NULL ");

		params.put("wgroup_id", wkey.getId());
		if(StringUtils.isNotBlank(uname)){
			
			SQL_FROM.append("AND (a.account like :uname OR a.email like :uname OR a.full_name like :uname) ");
			params.put("uname", "%" + StringUtils.trim(uname) + "%");
		}
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		String querysql = SQL_COLS.append(SQL_FROM).toString();
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + querysql + " / params : " + ArrayUtils.toString(params));
		}
		List<UserExInfo> result = null;
		try{
			result = jtemplate.query(querysql, params, userdao.getUserExRowMapper());
		}catch(DataAccessException dae){
			throw new ServiceException("Fail query members", dae);
		}

		return result;
	}

	@Override
	public PageWrapper<UserExInfo> getAvailableUsers(ServiceContext<?> svcctx, InfoId<Long> wkey, String uname, PageQuery pagequery) throws ServiceException {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.*, b.rel_id, c.* ");
		StringBuffer SQL_COUNT = new StringBuffer("SELECT count(a.user_id) ");
		StringBuffer SQL_FROM = new StringBuffer("FROM gp_users a ");
		SQL_FROM.append("LEFT JOIN (SELECT account,rel_id,workgroup_id from gp_workgroup_user WHERE workgroup_id = :wgroup_id ) b ")
				.append("ON b.account= a.account ")
				.append("LEFT JOIN ( SELECT instance_id, instance_name,short_name, abbr FROM gp_instances) c ")
				.append("ON a.source_id = c.instance_id ")
				.append("WHERE b.rel_id IS NULL ");

		params.put("wgroup_id", wkey.getId());
		if(StringUtils.isNotBlank(uname)){
			
			SQL_FROM.append("AND (a.account like :uname OR a.email like :uname OR a.full_name like :uname) ");
			params.put("uname", "%" + StringUtils.trim(uname) + "%");
		}
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		PageWrapper<UserExInfo> pwrapper = new PageWrapper<UserExInfo>();
		// get count sql scripts.
		String countsql = SQL_COUNT.append(SQL_FROM).toString();
		int totalrow = pseudodao.queryRowCount(jtemplate, countsql, params);
		// calculate pagination information, the page menu number is 5
		PaginationInfo pagination = new PaginationHelper(totalrow, 
				pagequery.getPageNumber(), 
				pagequery.getPageSize(), 5).getPaginationInfo();
		
		pwrapper.setPagination(pagination);
		
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL_COLS.append(SQL_FROM).toString(), pagequery);
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + pagesql + " / params : " + ArrayUtils.toString(params));
		}
		List<UserExInfo> result = null;
		try{
			result = jtemplate.query(pagesql, params, userdao.getUserExRowMapper());
			pwrapper.setRows(result);
		}catch(DataAccessException dae){
			throw new ServiceException("Fail query members", dae);
		}

		return pwrapper;
	}
	
	@Override
	public List<GroupInfo> getWorkgroupGroups(ServiceContext<?> svcctx, InfoId<Long> wkey, String gname) throws ServiceException {
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_groups ")
				.append("WHERE workgroup_id = :wgroup_id ");
		
		params.put("wgroup_id", wkey.getId());
		if(StringUtils.isNotBlank(gname)){
			
			SQL.append(" AND group_name LIKE :gname");
			params.put("gname", "%" + StringUtils.trim(gname) + "%");
		}
				
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);

		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<GroupInfo> result = null;
		try{
			result = jtemplate.query(SQL.toString(), params, groupdao.getRowMapper());
		}catch(DataAccessException dae){
			throw new ServiceException("Fail query members", dae);
		}

		return result;
	}

	@Transactional
	@Override
	public boolean addWorkgroupGroup(ServiceContext<?> svcctx, GroupInfo ginfo)
			throws ServiceException {
		
		svcctx.setTraceInfo(ginfo);
		
		try{
			groupdao.create(ginfo);
		}catch(DataAccessException dae){
			throw new ServiceException("Fail create group", dae);
		}
		return true;
	}

	@Transactional
	@Override
	public boolean removeWorkgroupGroup(ServiceContext<?> svcctx, InfoId<Long> wkey, String gname)
			throws ServiceException {
		
		try{
			return groupdao.deleteByName(wkey.getId(), gname) > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("Fail delte group", dae);
		}
	}

	@Transactional
	@Override
	public boolean removeWorkgroupGroup(ServiceContext<?> svcctx, InfoId<Long> groupid)
			throws ServiceException {
		try{
			groupuserdao.deleteByGroup(groupid.getId());
			return groupdao.delete(groupid) > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("Fail delete group", dae);
		}
	}

	@Transactional
	@Override
	public boolean addWorkgroupGroupMember(ServiceContext<?> svcctx, InfoId<Long> groupid, String... accounts)
			throws ServiceException {

		try{
			GroupInfo ginfo = groupdao.query(groupid);
			Long wgroupid = ginfo.getWorkgroupId();
			for(String account : accounts){
				
				GroupUserInfo guinfo = new GroupUserInfo();
				InfoId<Long> guid = idService.generateId(IdKey.GROUP_USER, Long.class);
				guinfo.setInfoId(guid);
				guinfo.setGroupId(groupid.getId());
				guinfo.setWorkgroupId(wgroupid);
				guinfo.setAccount(account);
				svcctx.setTraceInfo(guinfo);
				groupuserdao.create(guinfo);
			}
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail add workgroup member",dae);
		}
		return true;
	}

	@Override
	public List<UserInfo> getWorkgroupGroupMembers(ServiceContext<?> svcctx, InfoId<Long> groupid) throws ServiceException {


		Map<String,Object> params = new HashMap<String,Object>();
		
		StringBuffer SQL = new StringBuffer("SELECT b.* FROM gp_group_user a, gp_users b ")
				.append("WHERE a.account = b.account and a.group_id = :gid ");
		
		params.put("gid", groupid.getId());
				
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);

		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<UserInfo> result = null;
		try{
			result = jtemplate.query(SQL.toString(), params, userdao.getRowMapper());
		}catch(DataAccessException dae){
			throw new ServiceException("Fail query members", dae);
		}

		return result;
	}

	@Transactional
	@Override
	public boolean removeWorkgroupGroupMember(ServiceContext<?> svcctx, InfoId<Long> groupid, String... accounts)
			throws ServiceException {
		
		try{
			for(String account : accounts){				
				groupuserdao.deleteByAccount(groupid.getId(), account);
			}
		}catch(DataAccessException dae){
			throw new ServiceException("Fail remove members", dae);
		}
		return true;
	}

	@Override
	public List<WorkgroupExInfo> getLocalWorkgroups(ServiceContext<?> svcctx, String gname) throws ServiceException {
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.* ,b.*, c.* ");

		StringBuffer SQL_FROM = new StringBuffer("FROM gp_workgroups a ")
				.append("LEFT JOIN ( SELECT instance_id,instance_name,abbr,short_name,entity_code,node_code FROM gp_instances) b ON a.source_id = b.instance_id ")
				.append("LEFT JOIN ( SELECT account,full_name FROM gp_users) c ON a.admin = c.account ")
				.append("WHERE a.source_id = ").append(GeneralConstants.LOCAL_INSTANCE);
		
		if(StringUtils.isNotBlank(gname)){
			
			SQL_FROM.append(" AND a.workgroup_name LIKE :wgroup ");
			params.put("wgroup", "%" + StringUtils.trim(gname) + "%");
		}
				
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		String querysql = SQL_COLS.append(SQL_FROM.toString()).toString() ;
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + querysql + " / params : " + ArrayUtils.toString(params));
		}
		List<WorkgroupExInfo> result = null;
		try{
			result = jtemplate.query(querysql, params, workgroupdao.getWorkgroupExRowMapper());
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail query members", dae);
		}
		
		return result;
	}

	@Override
	public List<WorkgroupExInfo> getMirrorWorkgroups(ServiceContext<?> svcctx, String gname) throws ServiceException {
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.* ,b.*, c.* ");

		StringBuffer SQL_FROM = new StringBuffer("FROM gp_workgroups a ")
				.append("LEFT JOIN ( SELECT instance_id,instance_name,abbr,short_name,entity_code,node_code FROM gp_instances) b ON a.source_id = b.instance_id ")
				.append("LEFT JOIN ( SELECT account,full_name FROM gp_users) c ON a.admin = c.account ")
				.append("WHERE a.source_id <> ").append(GeneralConstants.LOCAL_INSTANCE);
		
		if(StringUtils.isNotBlank(gname)){
			
			SQL_FROM.append(" AND a.workgroup_name LIKE :wgroup ");
			params.put("wgroup", "%" + StringUtils.trim(gname) + "%");
		}

		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		String querysql = SQL_COLS.append(SQL_FROM.toString()).toString() ;
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + querysql + " / params : " + ArrayUtils.toString(params));
		}
		List<WorkgroupExInfo> result = null;
		try{
			result = jtemplate.query(querysql, params, workgroupdao.getWorkgroupExRowMapper());
		}catch(DataAccessException dae){
			throw new ServiceException("Fail query members", dae);
		}

		return result;
	}

	@Override
	public WorkgroupExInfo getWorkgroupEx(ServiceContext<?> svcctx, InfoId<Long> wkey) throws ServiceException {
		
		Object[] params = new Object[]{wkey.getId()};
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.* ,b.*, c.* ");

		StringBuffer SQL_FROM = new StringBuffer("FROM gp_workgroups a ")
				.append("LEFT JOIN ( SELECT instance_id,instance_name,abbr,short_name,entity_code,node_code FROM gp_instances) b ON a.source_id = b.instance_id ")
				.append("LEFT JOIN ( SELECT account,full_name FROM gp_users) c ON a.admin = c.account ")
				.append("WHERE a.source_id = ").append(GeneralConstants.LOCAL_INSTANCE)
				.append(" AND workgroup_id = ?");
				
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		String querysql = SQL_COLS.append(SQL_FROM.toString()).toString() ;
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + querysql + " / PARAMS : " + ArrayUtils.toString(params));
		}
		List<WorkgroupExInfo> result = null;
		try{
			result = jtemplate.query(querysql, params, workgroupdao.getWorkgroupExRowMapper());
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail query members", dae);
		}
		
		return CollectionUtils.isEmpty(result) ? null : result.get(0);
	}
}
