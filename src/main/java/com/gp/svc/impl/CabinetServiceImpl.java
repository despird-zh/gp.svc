package com.gp.svc.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.gp.acl.AcePrivilege;
import com.gp.acl.AceType;
import com.gp.common.Cabinets;
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.common.ServiceContext;
import com.gp.common.DataSourceHolder;
import com.gp.dao.CabFileDAO;
import com.gp.dao.CabFolderDAO;
import com.gp.dao.CabinetDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.dao.info.CabEntryInfo;
import com.gp.dao.info.CabFileInfo;
import com.gp.dao.info.CabFolderInfo;
import com.gp.dao.info.CabinetInfo;
import com.gp.info.InfoId;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.pagination.PaginationHelper;
import com.gp.pagination.PaginationInfo;
import com.gp.svc.CabinetService;

@Service
public class CabinetServiceImpl implements CabinetService{

	Logger LOGGER = LoggerFactory.getLogger(CabinetServiceImpl.class);

	@Autowired
	CabinetDAO cabinetdao;
	
	@Autowired
	CabFolderDAO cabfolderdao;
	
	@Autowired
	CabFileDAO cabfiledao;
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public CabinetInfo getCabinet(ServiceContext svcctx, InfoId<Long> ckey) throws ServiceException {

		try{
			
			return cabinetdao.query(ckey);
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "Cabinet", ckey);
		}
	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public List<CabFolderInfo> getCabFolders(ServiceContext svcctx, InfoId<Long> ckey, InfoId<Long> folderkey,
			String foldername) throws ServiceException {

		StringBuffer SQL = new StringBuffer();
		
		SQL.append("SELECT");
		SQL.append(" folders.*");
		SQL.append(" FROM");
		SQL.append(" gp_cab_folders folders");
		SQL.append(" WHERE folders.cabinet_id = :cabinet_id");
		SQL.append(" AND folders.folder_pid = :folder_pid");
		SQL.append(" AND folders.folder_name like :folder_name");
		SQL.append(" AND EXISTS(");
		SQL.append(" SELECT ace.ace_id");
		SQL.append(" FROM gp_cab_ace ace");
		SQL.append(" WHERE 	(");
		SQL.append(" (");
		SQL.append("	folders.OWNER = :subject");
		SQL.append("	AND ace.subject_type = '").append(AceType.OWNER.value).append("'");
		SQL.append("	AND ace.privilege & ").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value);
		SQL.append(" )");
		SQL.append(" OR (");
		SQL.append("	ace.subject_type = '").append(AceType.ANYONE.value).append("'");
		SQL.append("	AND ace.privilege & ").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value);
		SQL.append(" )");
		SQL.append(" OR (");
		SQL.append("	ace.subject_type = '").append(AceType.USER.value).append("'");
		SQL.append("	AND ace.privilege & ").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value);
		SQL.append("	AND ace.subject = :subject");
		SQL.append(" )");
		SQL.append(" )");
		SQL.append(" AND ace.acl_id = folders.acl_id");
		SQL.append(")");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("subject", svcctx.getPrincipal().getAccount());
		params.put("cabinet_id", ckey.getId());
		params.put("folder_pid", folderkey.getId());
		params.put("folder_name", StringUtils.isBlank(foldername)? "%": StringUtils.trim(foldername) + "%");

		NamedParameterJdbcTemplate jdbctemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		List<CabFolderInfo> result = null;
		try{
			if(LOGGER.isDebugEnabled())
				LOGGER.debug("SQL : {} / Params : {}",SQL.toString(),params.toString() );
			
			result = jdbctemplate.query(SQL.toString(), params, CabFolderDAO.CabFolderMapper);
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "Cabinet Folder", ckey);
		}
		return result;

	}
	
	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public PageWrapper<CabFolderInfo> getCabFolders(ServiceContext svcctx, InfoId<Long> ckey, InfoId<Long> folderkey,
			String foldername, PageQuery pagequery) throws ServiceException {
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT folders.* ");
		StringBuffer SQL_COUNT_COLS = new StringBuffer("SELECT count(folders.folder_id) ");
		
		StringBuffer SQL = new StringBuffer();
		SQL.append(" FROM");
		SQL.append(" gp_cab_folders folders");
		SQL.append(" WHERE folders.cabinet_id = :cabinet_id");
		SQL.append(" AND folders.folder_pid = :folder_pid");
		SQL.append(" AND folders.folder_name like :folder_name");
		SQL.append(" AND EXISTS(");
		SQL.append(" SELECT ace.ace_id");
		SQL.append(" FROM gp_cab_ace ace");
		SQL.append(" WHERE 	(");
		SQL.append(" (");
		SQL.append("	folders.OWNER = :subject");
		SQL.append("	AND ace.subject_type = '").append(AceType.OWNER.value).append("'");
		SQL.append("	AND ace.privilege & ").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value);
		SQL.append(" )");
		SQL.append(" OR (");
		SQL.append("	ace.subject_type = '").append(AceType.ANYONE.value).append("'");
		SQL.append("	AND ace.privilege & ").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value);
		SQL.append(" )");
		SQL.append(" OR (");
		SQL.append("	ace.subject_type = '").append(AceType.USER.value).append("'");
		SQL.append("	AND ace.privilege & ").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value);
		SQL.append("	AND ace.subject = :subject");
		SQL.append(" )");
		SQL.append(" )");
		SQL.append(" AND ace.acl_id = folders.acl_id");
		SQL.append(")");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("subject", svcctx.getPrincipal().getAccount());
		params.put("cabinet_id", ckey.getId());
		params.put("folder_pid", folderkey.getId());
		params.put("folder_name", StringUtils.isBlank(foldername)? "%": StringUtils.trim(foldername) + "%");
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		PageWrapper<CabFolderInfo> pwrapper = new PageWrapper<CabFolderInfo>();
		
		if(pagequery.isTotalCountEnable()){
			int totalrow = pseudodao.queryRowCount(jtemplate, SQL_COUNT_COLS.append(SQL).toString(), params);
			// calculate pagination information, the page menu number is 5
			PaginationInfo pagination = new PaginationHelper(totalrow, 
					pagequery.getPageNumber(), 
					pagequery.getPageSize(), 5).getPaginationInfo();
			
			pwrapper.setPagination(pagination);
		}
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL_COLS.append(SQL).toString(), pagequery);

		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + pagesql.toString() + " / params : " + params.toString());
		}
		
		List<CabFolderInfo> result = null;
		try{
			
			result = jtemplate.query(pagesql, params, CabFolderDAO.CabFolderMapper);
			
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "Cabinet Folder", ckey);
		}
		pwrapper.setRows(result);
		
		return pwrapper;

	}
	
	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public List<CabFileInfo> getCabFiles(ServiceContext svcctx, InfoId<Long> ckey, InfoId<Long> folderkey,
			String filename) throws ServiceException {

		StringBuffer SQL = new StringBuffer();
		
		SQL.append("SELECT");
		SQL.append(" files.*");
		SQL.append(" FROM");
		SQL.append(" gp_cab_files files");
		SQL.append(" WHERE files.cabinet_id = :cabinet_id");
		SQL.append(" AND files.folder_id = :folder_id");
		SQL.append(" AND files.file_name like :file_name");
		SQL.append(" AND EXISTS(");
		SQL.append(" SELECT ace.ace_id");
		SQL.append(" FROM gp_cab_ace ace");
		SQL.append(" WHERE 	(");
		SQL.append(" (");
		SQL.append("	files.OWNER = :subject");
		SQL.append("	AND ace.subject_type = '").append(AceType.OWNER.value).append("'");
		SQL.append("	AND ace.privilege & ").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value);
		SQL.append(" )");
		SQL.append(" OR (");
		SQL.append("	ace.subject_type = '").append(AceType.ANYONE.value).append("'");
		SQL.append("	AND ace.privilege & ").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value);
		SQL.append(" )");
		SQL.append(" OR (");
		SQL.append("	ace.subject_type = '").append(AceType.USER.value).append("'");
		SQL.append("	AND ace.privilege & ").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value);
		SQL.append("	AND ace.subject = :subject");
		SQL.append(" )");
		SQL.append(" )");
		SQL.append(" AND ace.acl_id = files.acl_id");
		SQL.append(")");
		
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("subject", svcctx.getPrincipal().getAccount());
		params.put("cabinet_id", ckey.getId());
		params.put("folder_id", folderkey.getId());
		params.put("file_name", StringUtils.isBlank(filename)? "%": StringUtils.trim(filename) + "%");

		NamedParameterJdbcTemplate jdbctemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		List<CabFileInfo> result = null;
		try{
			if(LOGGER.isDebugEnabled())
				LOGGER.debug("SQL : {} / Params : {}",SQL.toString(), params.toString() );
			
			result = jdbctemplate.query(SQL.toString(), params, CabFileDAO.CabFileMapper);
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "Cabinet File", ckey);
		}
		return result;

	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public PageWrapper<CabFileInfo> getCabFiles(ServiceContext svcctx, 
			InfoId<Long> ckey,
			InfoId<Long> folderkey,
			String filename, 
			PageQuery pagequery) throws ServiceException {
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT files.* ");
		StringBuffer SQL_COUNT_COLS = new StringBuffer("SELECT count(files.file_id) ");
		
		StringBuffer SQL = new StringBuffer();
		SQL.append(" FROM");
		SQL.append(" gp_cab_files files");
		SQL.append(" WHERE files.cabinet_id = :cabinet_id");
		SQL.append(" AND files.folder_id = :folder_id");
		SQL.append(" AND files.file_name like :file_name");
		SQL.append(" AND EXISTS(");
		SQL.append(" SELECT ace.ace_id");
		SQL.append(" FROM gp_cab_ace ace");
		SQL.append(" WHERE 	(");
		SQL.append(" (");
		SQL.append("	files.OWNER = :subject");
		SQL.append("	AND ace.subject_type = '").append(AceType.OWNER.value).append("'");
		SQL.append("	AND ace.privilege & ").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value);
		SQL.append(" )");
		SQL.append(" OR (");
		SQL.append("	ace.subject_type = '").append(AceType.ANYONE.value).append("'");
		SQL.append("	AND ace.privilege & ").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value);
		SQL.append(" )");
		SQL.append(" OR (");
		SQL.append("	ace.subject_type = '").append(AceType.USER.value).append("'");
		SQL.append("	AND ace.privilege & ").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value);
		SQL.append("	AND ace.subject = :subject");
		SQL.append(" )");
		SQL.append(" )");
		SQL.append(" AND ace.acl_id = files.acl_id");
		SQL.append(")");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("subject", svcctx.getPrincipal().getAccount());
		params.put("cabinet_id", ckey.getId());
		params.put("folder_id", folderkey.getId());
		params.put("file_name", StringUtils.isBlank(filename)? "%": StringUtils.trim(filename) + "%");
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		PageWrapper<CabFileInfo> pwrapper = new PageWrapper<CabFileInfo>();
		if(pagequery.isTotalCountEnable()){
			int totalrow = pseudodao.queryRowCount(jtemplate, SQL_COUNT_COLS.append(SQL).toString(), params);
			// calculate pagination information, the page menu number is 5
			PaginationInfo pagination = new PaginationHelper(totalrow, 
					pagequery.getPageNumber(), 
					pagequery.getPageSize(), 5).getPaginationInfo();
			
			pwrapper.setPagination(pagination);
		}
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL_COLS.append(SQL).toString(), pagequery);

		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + pagesql + " / params : " + params.toString());
		}
		
		List<CabFileInfo> result = null;
		try{
			
			result = jtemplate.query(pagesql.toString(), params, CabFileDAO.CabFileMapper);
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "Cabinet File", ckey);
		}
		pwrapper.setRows(result);
		
		return pwrapper;
	}
	
	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public PageWrapper<CabEntryInfo> getCabEntries(ServiceContext svcctx, 
			InfoId<Long> ckey,
			InfoId<Long> folderkey,
			String entryname, 
			PageQuery pagequery) throws ServiceException {
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT entries.* ");
		StringBuffer SQL_COUNT_COLS = new StringBuffer("SELECT count(distinct entries.entry_id, entries.entry_type) ");

		StringBuffer SQL = new StringBuffer();
		SQL.append(" FROM");
		SQL.append(" gp_cab_entries entries");
		SQL.append(" WHERE entries.cabinet_id = :cabinet_id");
		SQL.append(" AND entries.folder_pid = :folder_pid");
		SQL.append(" AND entries.entry_name like :entry_name");
		SQL.append(" AND EXISTS(");
		SQL.append(" SELECT ace.ace_id");
		SQL.append(" FROM gp_cab_ace ace");
		SQL.append(" WHERE 	(");
		SQL.append(" (");
		SQL.append("	entries.OWNER = :subject");
		SQL.append("	AND ace.subject_type = '").append(AceType.OWNER.value).append("'");
		SQL.append("	AND ace.privilege & ").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value);
		SQL.append(" )");
		SQL.append(" OR (");
		SQL.append("	ace.subject_type = '").append(AceType.ANYONE.value).append("'");
		SQL.append("	AND ace.privilege & ").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value);
		SQL.append(" )");
		SQL.append(" OR (");
		SQL.append("	ace.subject_type = '").append(AceType.USER.value).append("'");
		SQL.append("	AND ace.privilege & ").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value);
		SQL.append("	AND ace.subject = :subject");
		SQL.append(" )");
		SQL.append(" )");
		SQL.append(" AND ace.acl_id = entries.acl_id");
		SQL.append(") ");
		SQL.append("ORDER BY entry_type desc, entry_name ");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("subject", svcctx.getPrincipal().getAccount());
		params.put("cabinet_id", ckey.getId());
		params.put("folder_pid", folderkey.getId());
		params.put("entry_name", StringUtils.isBlank(entryname)? "%": StringUtils.trim(entryname) + "%");
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);

		PageWrapper<CabEntryInfo> pwrapper = new PageWrapper<CabEntryInfo>();
		// need to count total row
		if(pagequery.isTotalCountEnable()){
			int totalrow = pseudodao.queryRowCount(jtemplate, SQL_COUNT_COLS.append(SQL).toString(), params);
			// calculate pagination information, the page menu number is 5
			PaginationInfo pagination = new PaginationHelper(totalrow, 
					pagequery.getPageNumber(), 
					pagequery.getPageSize(), 5).getPaginationInfo();
			
			pwrapper.setPagination(pagination);
		}
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL_COLS.append(SQL).toString(), pagequery);

		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + pagesql + " / params : " + params.toString());
		}
		
		List<CabEntryInfo> result = null;
		try{
			
			result = jtemplate.query(pagesql.toString(), params, CabEntryMapper);
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "Cabinet Entry", ckey);
		}
		pwrapper.setRows(result);
		
		return pwrapper;
	}
	
	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public List<CabinetInfo> getCabinets(ServiceContext svcctx, String account) throws ServiceException {

		List<CabinetInfo> rtv = null;
		StringBuffer SQL = new StringBuffer();
		SQL.append("select * from gp_cabinets a, gp_users b ")
				.append("where (b.publish_cabinet_id = a.cabinet_id or b.netdisk_cabinet_id = a.cabinet_id) and ")
				.append("a.workgroup_id = ? and ")
				.append("b.account = ? ");
		
		
		JdbcTemplate jdbctemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		RowMapper<CabinetInfo> mapper = CabinetDAO.CabinetMapper;
		
		Object[] parms = new Object[]{GeneralConstants.PERSONAL_WORKGROUP, account};
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(parms));
		try{
			rtv = jdbctemplate.query(SQL.toString(), parms, mapper);
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with",dae, "Personal cabinets", "account:" + account);
		}
		return rtv;
	}

	public static RowMapper<CabEntryInfo> CabEntryMapper = new RowMapper<CabEntryInfo>(){

		@Override
		public CabEntryInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			CabEntryInfo info = null;
			
			String isfolder = rs.getString("entry_type");
			InfoId<Long> id = null;
			if(Cabinets.EntryType.FOLDER.name().equals(isfolder)){
				id = IdKeys.getInfoId(IdKey.GP_CAB_FOLDERS, rs.getLong("entry_id"));
			
				CabFolderInfo ref = new CabFolderInfo();
				info = ref;
				
				ref.setFolderCount(rs.getInt("folder_count"));
				ref.setFileCount(rs.getInt("file_count"));
				ref.setTotalSize(rs.getLong("size"));
				ref.setProfile(rs.getString("profile"));
				ref.setProperties(rs.getString("properties"));
				
			}else{
				id = IdKeys.getInfoId(IdKey.GP_CAB_FILES,rs.getLong("entry_id"));
				CabFileInfo ref = new CabFileInfo();
				info = ref;
				ref.setSize(rs.getLong("size"));
				ref.setCommentOn(rs.getBoolean("comment_on"));
				ref.setVersion(rs.getString("version"));
				ref.setVersionLabel(rs.getString("version_label"));
				ref.setProfile(rs.getString("profile"));
				ref.setProperties(rs.getString("properties"));
				ref.setState(rs.getString("state"));
				ref.setFormat(rs.getString("format"));
				ref.setBinaryId(rs.getLong("binary_id"));
			}
			
			info.setInfoId(id);

			info.setSourceId(rs.getInt("source_id"));
			info.setCabinetId(rs.getLong("cabinet_id"));
			info.setParentId(rs.getLong("folder_pid"));
			info.setEntryName(rs.getString("entry_name"));
			info.setDescription(rs.getString("descr"));
			info.setAclId(rs.getLong("acl_id"));
			info.setOwner(rs.getString("owner"));
			info.setHashCode(rs.getString("hash_code"));
			info.setOwm(rs.getLong("owm"));
			info.setCreator(rs.getString("creator"));
			info.setCreateDate(rs.getTimestamp("create_time"));
			info.setClassification(rs.getString("classification"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
	};

}
