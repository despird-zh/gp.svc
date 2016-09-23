package com.gp.svc.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gp.dao.impl.ImageDAOImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.Cabinets;
import com.gp.common.FlatColumns;
import com.gp.common.GeneralConstants;
import com.gp.common.GroupUsers;
import com.gp.common.IdKey;
import com.gp.common.Images;
import com.gp.common.SystemOptions;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.config.ServiceConfigurer;
import com.gp.common.ServiceContext;
import com.gp.dao.CabinetDAO;
import com.gp.dao.GroupDAO;
import com.gp.dao.GroupUserDAO;
import com.gp.dao.ImageDAO;
import com.gp.dao.PseudoDAO;
import com.gp.dao.UserDAO;
import com.gp.dao.WorkgroupDAO;
import com.gp.dao.WorkgroupSumDAO;
import com.gp.exception.ServiceException;
import com.gp.dao.info.CabinetInfo;
import com.gp.info.CombineInfo;
import com.gp.info.FlatColLocator;
import com.gp.dao.info.GroupInfo;
import com.gp.dao.info.GroupMemberInfo;
import com.gp.dao.info.GroupUserInfo;
import com.gp.dao.info.ImageInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.SysOptionInfo;
import com.gp.dao.info.UserInfo;
import com.gp.dao.info.WorkgroupInfo;
import com.gp.dao.info.WorkgroupSumInfo;
import com.gp.svc.info.WorkgroupLite;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.pagination.PaginationHelper;
import com.gp.pagination.PaginationInfo;
import com.gp.svc.CommonService;
import com.gp.svc.SystemService;
import com.gp.svc.WorkgroupService;
import com.gp.svc.info.UserExtInfo;
import com.gp.svc.info.WorkgroupExtInfo;
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
	CommonService idService;
	
	@Autowired 
	SystemService systemservice;
	
	@Autowired
	WorkgroupSumDAO workgroupsumdao;
	
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
	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean newWorkgroup(ServiceContext svcctx, WorkgroupInfo winfo, Long pubcapacity, Long pricapacity) throws ServiceException {
		
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
		pubinfo.setSourceId(winfo.getSourceId());
		pubinfo.setVersionable(versionable);

		if(null == pubcapacity){
			// set capacity 
			SysOptionInfo sysopt = systemservice.getOption(svcctx, SystemOptions.WORKGROUP_CABINET_QUOTA);
			pubcapacity = StringUtils.isNumeric(sysopt.getOptionValue()) ? Long.valueOf(sysopt.getOptionValue()) :512L;
			pubcapacity = pubcapacity * 1024 * 1024;// into bytes
		}
		pubinfo.setCapacity(pubcapacity);
		svcctx.setTraceInfo(pubinfo);
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
		priinfo.setSourceId(winfo.getSourceId());

		if(null == pricapacity){
			// set capacity 
			SysOptionInfo sysopt = systemservice.getOption(svcctx, SystemOptions.WORKGROUP_CABINET_QUOTA);
			pricapacity = StringUtils.isNumeric(sysopt.getOptionValue()) ? Long.valueOf(sysopt.getOptionValue()) :512L;
			pricapacity = pricapacity * 1024 * 1024;
		}

		// set capacity 
		priinfo.setCapacity(pricapacity);
		svcctx.setTraceInfo(priinfo);
		
		GroupInfo group = new GroupInfo();
		InfoId<Long> grpid = idService.generateId( IdKey.GROUP, Long.class);
		group.setInfoId(grpid);
		group.setGroupName("Workgroup's Member Group");
		group.setGroupType(GroupUsers.GroupType.WORKGROUP_MBR.name());
		group.setManageId(winfo.getInfoId().getId());
		winfo.setMemberGroupId(grpid.getId());
		svcctx.setTraceInfo(group);
		// create group user record
		GroupUserInfo mbrinfo= new GroupUserInfo();
		InfoId<Long> guid = idService.generateId(IdKey.GROUP_USER, Long.class);
		mbrinfo.setInfoId(guid);
		mbrinfo.setAccount(winfo.getManager());
		mbrinfo.setGroupId(grpid.getId());
		mbrinfo.setRole(GroupUsers.WorkgroupMemberRole.MANAGER.name());
		svcctx.setTraceInfo(mbrinfo);
		
		int cnt = 0;
		try{
			// create image firstly.		
			String imgpath = svcctx.getContextData(CTX_KEY_IMAGE_PATH, String.class);
			String filename = FilenameUtils.getName(imgpath);
			Long imgid = Images.parseImageId(filename);
			ImageInfo imginfo = imagedao.query(IdKey.IMAGE.getInfoId(imgid));
			// check if the image exists
			if(imginfo == null){ // save the image
				imginfo = ImageDAOImpl.parseLocalImageInfo(imgpath);
				imginfo.setCategory(Images.Category.WGROUP_AVATAR.name());
				svcctx.setTraceInfo(imginfo);
				imagedao.create(imginfo);
			}
			winfo.setAvatarId(imgid);			
			cnt = workgroupdao.create( winfo);
			cabinetdao.create( pubinfo);
			cabinetdao.create( priinfo);			
			groupdao.create(group);
			groupuserdao.create(mbrinfo);
		}catch(DataAccessException dae){

			throw new ServiceException("excp.create", dae, "workgroup");
		}
		return cnt > 0;
	}

	/**
	 * Change the workgroup and related cabinet storage quota information.
	 **/
	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean updateWorkgroup(ServiceContext svcctx, WorkgroupInfo winfo, Long pubcapacity, Long pricapacity) throws ServiceException {
		
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
				imginfo = ImageDAOImpl.parseLocalImageInfo(imgpath);
				imginfo.setCategory(Images.Category.WGROUP_AVATAR.name());
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
					
					if(null != pubcapacity){
						cabinetdao.updateCabCapacity(cab.getInfoId(), pubcapacity);
					}
					cabinetdao.changeStorage(cab.getInfoId(), storageId);
					winfo.setPublishCabinet(cab.getInfoId().getId());
				}
				if(Cabinets.CabinetType.NETDISK.name().equals(cab.getCabinetType())){
					
					if(null != pricapacity){
						cabinetdao.updateCabCapacity(cab.getInfoId(), pricapacity);
					}
					cabinetdao.changeStorage(cab.getInfoId(), storageId);
					winfo.setNetdiskCabinet(cab.getInfoId().getId());
				}
			}
			
			// update work group, exclude the member group id 
			cnt = workgroupdao.update( winfo,FilterMode.EXCLUDE, FlatColumns.MBR_GRP_ID);
			
		}catch(DataAccessException dae){

			throw new ServiceException("excp.update", dae, "workgroup");
		}
		return cnt > 0;
	}
	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public WorkgroupInfo getWorkgroup(ServiceContext svcctx, InfoId<Long> wkey) throws ServiceException {
		
		WorkgroupInfo winfo = null;

		winfo = workgroupdao.query( wkey);
		
		return winfo;

	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public List<GroupMemberInfo> getWorkgroupMembers(ServiceContext svcctx, InfoId<Long> wkey, 
			String uname , InfoId<Integer> sourceId) throws ServiceException {
	
		Map<String,Object> params = new HashMap<String,Object>();
		
		Long memberGroupId = pseudodao.query(wkey, FlatColumns.MBR_GRP_ID, Long.class);
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT * ");
		StringBuffer SQL_FROM = new StringBuffer("FROM gp_workgroup_mbrs ");
		
		SQL_FROM.append("WHERE group_id = :group_id");
		params.put("group_id", memberGroupId);
		
		if(StringUtils.isNotBlank(uname)){
			
			SQL_FROM.append("AND (account like :uname OR email like :uname OR full_name like :uname) ");
			params.put("uname", "%" + StringUtils.trim(uname) + "%");
		}
		if(InfoId.isValid(sourceId)){
			
			SQL_FROM.append("AND source_id = :source_id ");
			params.put("source_id", sourceId.getId());
		}
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		String querysql = SQL_COLS.append(SQL_FROM).toString();
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + querysql + " / params : " + ArrayUtils.toString(params));
		}
		List<GroupMemberInfo> result = null;
		try{
			result = jtemplate.query(querysql, params, GroupUserDAO.GroupMemberMapper);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "workgroup members", wkey);
		}

		return result;
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public PageWrapper<GroupMemberInfo> getWorkgroupMembers(ServiceContext svcctx, InfoId<Long> wkey, 
			String uname , InfoId<Integer> sourceId, PageQuery pagequery) throws ServiceException {
	
		Map<String,Object> params = new HashMap<String,Object>();
		Long memberGroupId = pseudodao.query(wkey, FlatColumns.MBR_GRP_ID, Long.class);
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT * ");
		StringBuffer SQL_COUNT = new StringBuffer("SELECT COUNT(user_id) ");
		StringBuffer SQL_FROM = new StringBuffer("FROM gp_workgroup_mbrs ");
		
		SQL_FROM.append("WHERE group_id = :group_id ");
		params.put("group_id", memberGroupId);
		
		if(StringUtils.isNotBlank(uname)){
			
			SQL_FROM.append("AND (account like :uname OR email like :uname OR full_name like :uname) ");
			params.put("uname", "%" + StringUtils.trim(uname) + "%");
		}
		if(InfoId.isValid(sourceId)){
			
			SQL_FROM.append("AND source_id = :source_id ");
			params.put("source_id", sourceId.getId());
		}
		SQL_FROM.append("ORDER BY mbr_rel_id");
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		PageWrapper<GroupMemberInfo> pwrapper = new PageWrapper<GroupMemberInfo>();
		if(pagequery.isTotalCountEnable()){
			// get count sql scripts.
			String countsql = SQL_COUNT.append(SQL_FROM).toString();
			int totalrow = pseudodao.queryRowCount(jtemplate, countsql, params);
			// calculate pagination information, the page menu number is 5
			PaginationInfo pagination = new PaginationHelper(totalrow, 
					pagequery.getPageNumber(), 
					pagequery.getPageSize(), 5).getPaginationInfo();
			
			pwrapper.setPagination(pagination);
		}
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL_COLS.append(SQL_FROM).toString(), pagequery);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + pagesql + " / params : " + ArrayUtils.toString(params));
		}
		List<GroupMemberInfo> result = null;
		try{
			result = jtemplate.query(pagesql, params, GroupUserDAO.GroupMemberMapper);
			pwrapper.setRows(result);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "workgroup members", wkey);
		}

		return pwrapper;
	}
	
	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean addWorkgroupMember(ServiceContext svcctx,InfoId<Long> wkey, GroupUserInfo memberinfo)
			throws ServiceException {
		try{
			InfoId<Long> grpid = null; 
			if(null == memberinfo.getGroupId() || memberinfo.getGroupId() <= 0){
				Long val = pseudodao.query(wkey, FlatColumns.MBR_GRP_ID,  Long.class);
				grpid = IdKey.GROUP.getInfoId(val);
				memberinfo.setGroupId(grpid.getId());
			}
			InfoId<Long> mbrid = groupuserdao.existByAccount(grpid, memberinfo.getAccount());
	
			if(InfoId.isValid(mbrid)){
				// member already existed, update 
				Map<FlatColLocator, Object> cols = new HashMap<FlatColLocator, Object>();
				cols.put(FlatColumns.MBR_ROLE, memberinfo.getRole());
				cols.put(FlatColumns.MODIFIER, svcctx.getPrincipal().getAccount());
				cols.put(FlatColumns.MODIFY_DATE, DateTimeUtils.now());				

				pseudodao.update(mbrid, cols);
			}else{
				// not exist, then create new record
				svcctx.setTraceInfo(memberinfo);				
				// prepare new record
				InfoId<Long> rid = idService.generateId( IdKey.GROUP_USER, Long.class);
				memberinfo.setInfoId(rid);				
				groupuserdao.create( memberinfo);
			}
		}catch(DataAccessException dae){
			throw new ServiceException("excp.add.mbr", dae, memberinfo.getAccount(), wkey);
		}
		return true;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean removeWorkgroupMember(ServiceContext svcctx, InfoId<Long> wkey, String account)
			throws ServiceException {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("Delete from gp_group_user ");
		SQL.append(" where group_id in (select group_id from gp_groups where workgroup_id = ? ) ");
		SQL.append(" AND account = ?");
		
		Object[] params = new Object[]{wkey.getId(), account};
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		try{
			return jtemplate.update(SQL.toString(), params) > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.remove.mbr", dae, account, wkey);
		}
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public List<UserExtInfo> getAvailableUsers(ServiceContext svcctx, InfoId<Long> wkey, String uname) throws ServiceException {
		
		Map<String,Object> params = new HashMap<String,Object>();
		Long memberGroupId = pseudodao.query(wkey, FlatColumns.MBR_GRP_ID, Long.class);
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.*, b.rel_id, c.* ");

		StringBuffer SQL_FROM = new StringBuffer("FROM gp_users a ");
		SQL_FROM.append("LEFT JOIN (SELECT account,rel_id from gp_group_user WHERE group_id = :group_id ) b ")
				.append("ON b.account= a.account ")
				.append("LEFT JOIN ( SELECT source_id, source_name,short_name, abbr FROM gp_sources) c ")
				.append("ON a.source_id = c.source_id ")
				.append("WHERE b.rel_id IS NULL ");

		params.put("group_id", memberGroupId);
		
		if(StringUtils.isNotBlank(uname)){
			
			SQL_FROM.append("AND (a.account like :uname OR a.email like :uname OR a.full_name like :uname) ");
			params.put("uname", "%" + StringUtils.trim(uname) + "%");
		}
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		String querysql = SQL_COLS.append(SQL_FROM).toString();
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + querysql + " / params : " + ArrayUtils.toString(params));
		}
		List<UserExtInfo> result = null;
		try{
			result = jtemplate.query(querysql, params, SecurityServiceImpl.UserExMapper);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query", dae, "workgroup members");
		}

		return result;
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public PageWrapper<UserExtInfo> getAvailableUsers(ServiceContext svcctx, InfoId<Long> wkey, String uname, PageQuery pagequery) throws ServiceException {
		
		Map<String,Object> params = new HashMap<String,Object>();
		Long memberGroupId = pseudodao.query(wkey, FlatColumns.MBR_GRP_ID, Long.class);

		StringBuffer SQL_COLS = new StringBuffer("SELECT a.*, b.rel_id, c.* ");
		StringBuffer SQL_COUNT = new StringBuffer("SELECT count(a.user_id) ");
		StringBuffer SQL_FROM = new StringBuffer("FROM gp_users a ");
		SQL_FROM.append("LEFT JOIN (SELECT account,rel_id from gp_group_user WHERE group_id = :group_id ) b ")
				.append("ON b.account= a.account ")
				.append("LEFT JOIN ( SELECT source_id, source_name,short_name, abbr FROM gp_sources) c ")
				.append("ON a.source_id = c.source_id ")
				.append("WHERE b.rel_id IS NULL ");

		params.put("group_id", memberGroupId);
		if(StringUtils.isNotBlank(uname)){
			
			SQL_FROM.append("AND (a.account like :uname OR a.email like :uname OR a.full_name like :uname) ");
			params.put("uname", "%" + StringUtils.trim(uname) + "%");
		}
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		PageWrapper<UserExtInfo> pwrapper = new PageWrapper<UserExtInfo>();
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
		List<UserExtInfo> result = null;
		try{
			result = jtemplate.query(pagesql, params, SecurityServiceImpl.UserExMapper);
			pwrapper.setRows(result);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query", dae, "workgroup members");
		}

		return pwrapper;
	}
	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public List<GroupInfo> getWorkgroupGroups(ServiceContext svcctx, InfoId<Long> wkey, String gname) throws ServiceException {
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_groups ")
				.append("WHERE workgroup_id = :wgroup_id AND group_type = :group_type");
		
		params.put("wgroup_id", wkey.getId());
		params.put("group_type", GroupUsers.GroupType.WORKGROUP_GRP.name());
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
			result = jtemplate.query(SQL.toString(), params, GroupDAO.GroupMapper);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query", dae, "workgroup groups");
		}

		return result;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean addWorkgroupGroup(ServiceContext svcctx, GroupInfo ginfo)
			throws ServiceException {
		
		int cnt = 0;
		
		try{		
			ginfo.setGroupType(GroupUsers.GroupType.WORKGROUP_GRP.name());
			InfoId<Long> wgoupId = IdKey.WORKGROUP.getInfoId(ginfo.getManageId());
			GroupInfo orig = groupdao.queryByName(wgoupId,GroupUsers.GroupType.WORKGROUP_GRP.name(), ginfo.getGroupName());
			if(null != orig){
				
				orig.setGroupName(ginfo.getGroupName());
				orig.setDescription(ginfo.getDescription());
				svcctx.setTraceInfo(orig);
				
				cnt = groupdao.update(orig,FilterMode.NONE);
			}else{
				
				svcctx.setTraceInfo(ginfo);
				cnt = groupdao.create(ginfo);
			}
			
		}catch(DataAccessException dae){
			throw new ServiceException("excp.create", dae, "workgroup group");
		}
		return cnt > 0;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean removeWorkgroupGroup(ServiceContext svcctx, InfoId<Long> wkey, String gname)
			throws ServiceException {
		
		try{
			// locate the id of group name.
			GroupInfo ginfo = groupdao.queryByName(wkey,GroupUsers.GroupType.WORKGROUP_GRP.name(), gname);
			groupuserdao.deleteByGroup(ginfo.getInfoId());
			return groupdao.deleteByName(wkey,GroupUsers.GroupType.WORKGROUP_GRP.name(), gname) > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.delete", dae, "workgroup group");
		}
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean removeWorkgroupGroup(ServiceContext svcctx, InfoId<Long> groupid)
			throws ServiceException {
		try{
			groupuserdao.deleteByGroup(groupid);
			return groupdao.delete(groupid) > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("Fail delete group", dae);
		}
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean addWorkgroupGroupMember(ServiceContext svcctx, InfoId<Long> groupid, String... accounts)
			throws ServiceException {

		try{

			for(String account : accounts){
				
				InfoId<Long> mbrId = groupuserdao.existByAccount(groupid, account);
				if(InfoId.isValid(mbrId)) {
					LOGGER.debug("User : {} already exist in group : {}", account, groupid.toString());
					continue;
				}
				GroupUserInfo guinfo = new GroupUserInfo();
				InfoId<Long> guid = idService.generateId(IdKey.GROUP_USER, Long.class);
				guinfo.setInfoId(guid);
				guinfo.setGroupId(groupid.getId());
				guinfo.setAccount(account);
				svcctx.setTraceInfo(guinfo);
				groupuserdao.create(guinfo);
			}
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.add.mbr", dae, "member", "workgroup group");
		}
		return true;
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public List<UserInfo> getWorkgroupGroupMembers(ServiceContext svcctx, InfoId<Long> groupid) throws ServiceException {


		Map<String,Object> params = new HashMap<String,Object>();
		
		StringBuffer SQL = new StringBuffer("SELECT b.* FROM gp_group_user a, gp_users b ")
				.append("WHERE a.account = b.account and a.group_id = :group_id ");
		
		params.put("group_id", groupid.getId());
				
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);

		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<UserInfo> result = null;
		try{
			result = jtemplate.query(SQL.toString(), params, UserDAO.UserMapper);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query", dae, "workgroup group members");
		}

		return result;
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public boolean removeWorkgroupGroupMember(ServiceContext svcctx, InfoId<Long> groupid, String... accounts)
			throws ServiceException {
		
		try{
			for(String account : accounts){				
				groupuserdao.deleteByAccount(groupid, account);
			}
		}catch(DataAccessException dae){
			throw new ServiceException("excp.remove.mbr", dae, "member", "workgroup group");
		}
		return true;
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public List<WorkgroupExtInfo> getLocalWorkgroups(ServiceContext svcctx, String gname) throws ServiceException {
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.* ,b.*, c.*,d.* ");

		StringBuffer SQL_FROM = new StringBuffer("FROM gp_workgroups a ")
				.append("LEFT JOIN ( SELECT source_id,source_name,abbr,short_name,entity_code,node_code FROM gp_sources) b ON a.source_id = b.source_id ")
				.append("LEFT JOIN ( SELECT account,full_name FROM gp_users) c ON a.admin = c.account ")
				.append("LEFT JOIN ( SELECT account,full_name as mgr_name FROM gp_users) d ON a.manager = d.account ")
				.append("WHERE a.source_id = ").append(GeneralConstants.LOCAL_SOURCE);
		
		if(StringUtils.isNotBlank(gname)){
			
			SQL_FROM.append(" AND a.workgroup_name LIKE :wgroup ");
			params.put("wgroup", "%" + StringUtils.trim(gname) + "%");
		}
		SQL_FROM.append(" ORDER BY a.workgroup_name");
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		String querysql = SQL_COLS.append(SQL_FROM.toString()).toString() ;
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + querysql + " / params : " + ArrayUtils.toString(params));
		}
		List<WorkgroupExtInfo> result = null;
		try{
			result = jtemplate.query(querysql, params, WorkgroupExMapper);
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query", dae , "local workgroups");
		}
		
		return result;
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public List<WorkgroupExtInfo> getMirrorWorkgroups(ServiceContext svcctx, String gname) throws ServiceException {
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.* ,b.*, c.*,d.* ");

		StringBuffer SQL_FROM = new StringBuffer("FROM gp_workgroups a ")
				.append("LEFT JOIN ( SELECT source_id,source_name,abbr,short_name,entity_code,node_code FROM gp_sources) b ON a.source_id = b.source_id ")
				.append("LEFT JOIN ( SELECT account,full_name FROM gp_users) c ON a.admin = c.account ")
				.append("LEFT JOIN ( SELECT account,full_name as mgr_name FROM gp_users) d ON a.manager = d.account ")
				.append("WHERE a.source_id <> ").append(GeneralConstants.LOCAL_SOURCE);
		
		if(StringUtils.isNotBlank(gname)){
			
			SQL_FROM.append(" AND a.workgroup_name LIKE :wgroup ");
			params.put("wgroup", "%" + StringUtils.trim(gname) + "%");
		}
		SQL_FROM.append(" ORDER BY a.workgroup_name");
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		String querysql = SQL_COLS.append(SQL_FROM.toString()).toString() ;
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + querysql + " / params : " + ArrayUtils.toString(params));
		}
		List<WorkgroupExtInfo> result = null;
		try{
			result = jtemplate.query(querysql, params, WorkgroupExMapper);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query", dae, "remote workgroups");
		}

		return result;
	}

	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public WorkgroupExtInfo getWorkgroupExt(ServiceContext svcctx, InfoId<Long> wkey) throws ServiceException {
		
		Object[] params = new Object[]{wkey.getId()};
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.* ,b.*, c.*,d.* ");

		StringBuffer SQL_FROM = new StringBuffer("FROM gp_workgroups a ")
				.append("LEFT JOIN ( SELECT source_id,source_name,abbr,short_name,entity_code,node_code FROM gp_sources) b ON a.source_id = b.source_id ")
				.append("LEFT JOIN ( SELECT account,full_name FROM gp_users) c ON a.admin = c.account ")
				.append("LEFT JOIN ( SELECT account,full_name as mgr_name FROM gp_users) d ON a.manager = d.account ")
				.append("WHERE a.source_id = ").append(GeneralConstants.LOCAL_SOURCE)
				.append(" AND workgroup_id = ?");
				
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		String querysql = SQL_COLS.append(SQL_FROM.toString()).toString() ;
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + querysql + " / PARAMS : " + ArrayUtils.toString(params));
		}
		List<WorkgroupExtInfo> result = null;
		try{
			result = jtemplate.query(querysql, params, WorkgroupExMapper);
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query", dae,"workgroup full information");
		}
		
		return CollectionUtils.isEmpty(result) ? null : result.get(0);
	}

	
	@Transactional(value = ServiceConfigurer.TRNS_MGR, readOnly=true)
	@Override
	public PageWrapper<CombineInfo<WorkgroupInfo,WorkgroupLite>> getLocalWorkgroups(ServiceContext svcctx, String gname,List<String> tags, PageQuery pagequery)
			throws ServiceException {
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT a.*,b.*,c.full_name ");
		
		StringBuffer SQL_COUNT = new StringBuffer("SELECT COUNT(a.workgroup_id) ");
		
		StringBuffer SQL_FROM = new StringBuffer("FROM gp_workgroups a ");		
		SQL_FROM.append(" LEFT JOIN (select image_id, image_format, image_name, image_link from gp_images) b")
				.append("    ON a.avatar_id = b.image_id ")
				.append(" LEFT JOIN (SELECT account, full_name FROM gp_users WHERE source_id = -9999) c")
				.append("    ON c.account = a.admin")
				.append(" WHERE a.source_id = ").append(GeneralConstants.LOCAL_SOURCE)
				.append("  AND (a.workgroup_name LIKE :wgname OR a.descr LIKE :wgname) ");		
		params.put("wgname", gname+"%");
		
		if(CollectionUtils.isNotEmpty(tags)){			
			SQL_FROM.append(" AND EXISTS ( SELECT tag_name from gp_tag_rel ");
			SQL_FROM.append("            WHERE resource_type = 'gp_workgroups' ");
			SQL_FROM.append("               AND workgroup_id = a.workgroup_id");
			SQL_FROM.append("               AND tag_name in( :tags)) ");
			params.put("tags", tags);
		}
		SQL_FROM.append(" ORDER BY a.workgroup_id DESC, a.workgroup_name");
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		PageWrapper<CombineInfo<WorkgroupInfo,WorkgroupLite>> pwrapper = new PageWrapper<CombineInfo<WorkgroupInfo,WorkgroupLite>>();
		if(pagequery.isTotalCountEnable()){
			// get count sql scripts.
			String countsql = SQL_COUNT.append(SQL_FROM).toString();
			int totalrow = pseudodao.queryRowCount(jtemplate, countsql, params);
			// calculate pagination information, the page menu number is 5
			PaginationInfo pagination = new PaginationHelper(totalrow, 
					pagequery.getPageNumber(), 
					pagequery.getPageSize(), 5).getPaginationInfo();
			
			pwrapper.setPagination(pagination);
		}
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL_COLS.append(SQL_FROM).toString(), pagequery);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + pagesql + " / params : " + params.toString());
		}
		List<CombineInfo<WorkgroupInfo,WorkgroupLite>> result = null;
		try{
			result = jtemplate.query(pagesql, params, WorkgroupLiteMapper);
			pwrapper.setRows(result);
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query", dae ,"workgroup");
		}

		return pwrapper;
	}

	@Override
	public WorkgroupSumInfo getWorkgroupSummary(ServiceContext svcctx, InfoId<Long> wkey) throws ServiceException {
		
		try{
			
			return workgroupsumdao.queryByWId(wkey);
			
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query", dae ,"workgroup summary");
		}
	}

}
