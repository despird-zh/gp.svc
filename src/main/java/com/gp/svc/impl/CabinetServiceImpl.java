package com.gp.svc.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.gp.acl.AcePrivilege;
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.dao.CabFileDAO;
import com.gp.dao.CabFolderDAO;
import com.gp.dao.CabinetDAO;
import com.gp.dao.PseudoDAO;
import com.gp.dao.impl.DAOSupport;
import com.gp.exception.ServiceException;
import com.gp.info.CabEntryInfo;
import com.gp.info.CabFileInfo;
import com.gp.info.CabFolderInfo;
import com.gp.info.CabinetInfo;
import com.gp.info.InfoId;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.pagination.PaginationHelper;
import com.gp.pagination.PaginationInfo;
import com.gp.svc.CabinetService;

@Service("cabinetService")
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
	
	@Override
	public CabinetInfo getCabinet(ServiceContext<?> svcctx, InfoId<Long> ckey) throws ServiceException {

		try{
			
			return cabinetdao.query(ckey);
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail get the cabinet information", dae);
		}
	}

	@Override
	public List<CabFolderInfo> getCabFolders(ServiceContext<?> svcctx, InfoId<Long> ckey, InfoId<Long> folderkey)
			throws ServiceException {
		
		return getCabFolders(svcctx, ckey, folderkey,null);
	}

	@Override
	public List<CabFolderInfo> getCabFolders(ServiceContext<?> svcctx, InfoId<Long> ckey, InfoId<Long> folderkey,
			String foldername) throws ServiceException {

		StringBuffer filterbuf = new StringBuffer();
		
		filterbuf.append("SELECT distinct a.folder_id ")
				.append("FROM gp_cab_folders a LEFT JOIN gp_cab_ace b ON a.acl_id = b.acl_id ")
				.append("WHERE ")
				.append("( ");
		/** ( ... OR ... OR ... OR ... OR ...) AND () */
		/** set owner filter condition */
		filterbuf.append("  ( a.owner = ? AND b.subject_type = 'o' AND ")
					      .append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value).append(" ) OR ");
		/** set group filter condition*/
		if(!CollectionUtils.isEmpty(svcctx.getPrincipal().getGroups())){
			filterbuf.append("  ( b.subject_type = 'g' AND ").append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value)
						.append(" AND b.subject IN ( ").append(DAOSupport.getInClause(svcctx.getPrincipal().getGroups())).append(" )) OR ");
		}
		/** set everyone filter condition */
		filterbuf.append("  ( b.subject_type = 'e' AND ").append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value).append(" )  OR ");
		/** set user filter condition */
		filterbuf.append("  ( b.subject_type = 'u' AND ").append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value)
					.append(" AND b.subject = ? ))");		
		filterbuf.append(" AND a.folder_pid = ? "); // folder id
		filterbuf.append(" AND a.cabinet_id = ? "); // cabinet id
		filterbuf.append(" AND a.folder_name like ? "); // folder name
		
		StringBuffer folderbuf = new StringBuffer("SELECT f0.* FROM gp_cab_folders f0,");
			folderbuf.append("(").append(filterbuf).append(") f1 ")
			.append("WHERE f0.folder_id = f1.folder_id ");
		
		Object[] params = new Object[]{
			svcctx.getPrincipal().getAccount(),
			svcctx.getPrincipal().getAccount(),
			folderkey.getId(),
			ckey.getId(),
			StringUtils.isBlank(foldername)? "%": StringUtils.trim(foldername) + "%"
		};

		JdbcTemplate jdbctemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		List<CabFolderInfo> result = null;
		try{
			if(LOGGER.isDebugEnabled())
				LOGGER.debug("SQL : {} / Params : {}",folderbuf.toString(), ArrayUtils.toString(params) );
			
			result = jdbctemplate.query(folderbuf.toString(), params, cabfolderdao.getRowMapper());
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("fail query folders in cabinet", dae);
		}
		return result;

	}
	
	@Override
	public PageWrapper<CabFolderInfo> getCabFolders(ServiceContext<?> svcctx, InfoId<Long> ckey, InfoId<Long> folderkey,
			String foldername, PageQuery pagequery) throws ServiceException {
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT distinct a.folder_id ");
		StringBuffer SQL_COUNT_COLS = new StringBuffer("SELECT count(distinct a.folder_id) ");
		
		StringBuffer filterbuf = new StringBuffer();
		
		filterbuf.append("FROM gp_cab_folders a LEFT JOIN gp_cab_ace b ON a.acl_id = b.acl_id ")
					.append("WHERE ")
					.append("( ");
		/** ( ... OR ... OR ... OR ... OR ...) AND () */
		/** set owner filter condition */
		filterbuf.append("  ( a.owner = ? AND b.subject_type = 'o' AND ")
					      .append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value).append(" ) OR ");
		/** set group filter condition*/
		if(!CollectionUtils.isEmpty(svcctx.getPrincipal().getGroups())){
			filterbuf.append("  ( b.subject_type = 'g' AND ").append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value)
						.append(" AND b.subject IN ( ").append(DAOSupport.getInClause(svcctx.getPrincipal().getGroups())).append(" )) OR ");
		}
		/** set everyone filter condition */
		filterbuf.append("  ( b.subject_type = 'e' AND ").append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value).append(" )  OR ");
		/** set user filter condition */
		filterbuf.append("  ( b.subject_type = 'u' AND ").append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value)
					.append(" AND b.subject = ? ))");		
		filterbuf.append(" AND a.folder_pid = ? "); // folder id
		filterbuf.append(" AND a.cabinet_id = ? "); // cabinet id
		filterbuf.append(" AND a.folder_name like ? "); // folder name
		// find all records
		SQL_COLS.append(filterbuf);

		Object[] params = new Object[]{
			svcctx.getPrincipal().getAccount(),
			svcctx.getPrincipal().getAccount(),
			folderkey.getId(),
			ckey.getId(),
			StringUtils.isBlank(foldername)? "%": StringUtils.trim(foldername) + "%"
		};
		
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		PageWrapper<CabFolderInfo> pwrapper = new PageWrapper<CabFolderInfo>();
		
		int totalrow = pseudodao.queryRowCount(jtemplate, SQL_COUNT_COLS.append(filterbuf).toString(), params);
		// calculate pagination information, the page menu number is 5
		PaginationInfo pagination = new PaginationHelper(totalrow, 
				pagequery.getPageNumber(), 
				pagequery.getPageSize(), 5).getPaginationInfo();
		
		pwrapper.setPagination(pagination);
		
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL_COLS.toString(), pagequery);

		StringBuffer folderbuf = new StringBuffer("SELECT f0.* FROM gp_cab_folders f0,");
		folderbuf.append("(").append(pagesql).append(") f1 ")
				.append("WHERE f0.folder_id = f1.folder_id ");
	
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + folderbuf + " / params : " + ArrayUtils.toString(params));
		}
		
		List<CabFolderInfo> result = null;
		try{
			
			result = jtemplate.query(folderbuf.toString(), params, cabfolderdao.getRowMapper());
			
		}catch(DataAccessException dae){
			throw new ServiceException("Fail query folders in cabinet", dae);
		}
		pwrapper.setRows(result);
		
		return pwrapper;

	}
	
	@Override
	public List<CabFileInfo> getCabFiles(ServiceContext<?> svcctx, InfoId<Long> ckey, InfoId<Long> folderkey)
			throws ServiceException {
		
		return getCabFiles(svcctx, ckey, folderkey, null);
	}
	
	@Override
	public List<CabFileInfo> getCabFiles(ServiceContext<?> svcctx, InfoId<Long> ckey, InfoId<Long> folderkey,
			String filename) throws ServiceException {

		StringBuffer filterbuf = new StringBuffer();
		
		filterbuf.append("SELECT distinct a.file_id ")
				.append("FROM gp_cab_files a LEFT JOIN gp_cab_ace b ON a.acl_id = b.acl_id ")
				.append("WHERE ")
				.append("( ");
		/** ( ... OR ... OR ... OR ... OR ...) AND () */
		/** set owner filter condition */
		filterbuf.append("  ( a.owner = ? AND b.subject_type = 'o' AND ")
					      .append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value).append(" ) OR ");
		/** set group filter condition*/
		if(!CollectionUtils.isEmpty(svcctx.getPrincipal().getGroups())){
			filterbuf.append("  ( b.subject_type = 'g' AND ").append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value)
						.append(" AND b.subject IN ( ").append(DAOSupport.getInClause(svcctx.getPrincipal().getGroups())).append(" )) OR ");
		}
		/** set everyone filter condition */
		filterbuf.append("  ( b.subject_type = 'e' AND ").append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value).append(" )  OR ");
		/** set user filter condition */
		filterbuf.append("  ( b.subject_type = 'u' AND ").append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value)
					.append(" AND b.subject = ? ))");		
		filterbuf.append(" AND a.folder_id = ? "); // folder id
		filterbuf.append(" AND a.cabinet_id = ? "); // cabinet id
		filterbuf.append(" AND a.file_name like ? "); // folder name
		
		StringBuffer folderbuf = new StringBuffer("SELECT f0.* FROM gp_cab_files f0,");
			folderbuf.append("(").append(filterbuf).append(") f1 ")
			.append("WHERE f0.file_id = f1.file_id ");
		
		Object[] params = new Object[]{
			svcctx.getPrincipal().getAccount(),
			svcctx.getPrincipal().getAccount(),
			folderkey.getId(),
			ckey.getId(),
			StringUtils.isBlank(filename)? "%": StringUtils.trim(filename) + "%"
		};

		JdbcTemplate jdbctemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		List<CabFileInfo> result = null;
		try{
			if(LOGGER.isDebugEnabled())
				LOGGER.debug("SQL : {} / Params : {}",folderbuf.toString(), ArrayUtils.toString(params) );
			
			result = jdbctemplate.query(folderbuf.toString(), params, cabfiledao.getRowMapper());
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("fail query files in cabinet", dae);
		}
		return result;

	}

	@Override
	public PageWrapper<CabFileInfo> getCabFiles(ServiceContext<?> svcctx, 
			InfoId<Long> ckey,
			InfoId<Long> folderkey,
			String filename, 
			PageQuery pagequery) throws ServiceException {
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT distinct a.file_id ");
		StringBuffer SQL_COUNT_COLS = new StringBuffer("SELECT count(distinct a.file_id) ");
		
		StringBuffer filterbuf = new StringBuffer();
		
		filterbuf.append("FROM gp_cab_files a LEFT JOIN gp_cab_ace b ON a.acl_id = b.acl_id ")
					.append("WHERE ")
					.append("( ");
		/** ( ... OR ... OR ... OR ... OR ...) AND () */
		/** set owner filter condition */
		filterbuf.append("  ( a.owner = ? AND b.subject_type = 'o' AND ")
					      .append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value).append(" ) OR ");
		/** set group filter condition*/
		if(!CollectionUtils.isEmpty(svcctx.getPrincipal().getGroups())){
			filterbuf.append("  ( b.subject_type = 'g' AND ").append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value)
						.append(" AND b.subject IN ( ").append(DAOSupport.getInClause(svcctx.getPrincipal().getGroups())).append(" )) OR ");
		}
		/** set everyone filter condition */
		filterbuf.append("  ( b.subject_type = 'e' AND ").append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value).append(" )  OR ");
		/** set user filter condition */
		filterbuf.append("  ( b.subject_type = 'u' AND ").append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value)
					.append(" AND b.subject = ? ))");		
		filterbuf.append(" AND a.folder_id = ? "); // folder id
		filterbuf.append(" AND a.cabinet_id = ? "); // cabinet id
		filterbuf.append(" AND a.file_name like ? "); // folder name
		// find all records
		SQL_COLS.append(filterbuf);

		Object[] params = new Object[]{
			svcctx.getPrincipal().getAccount(),
			svcctx.getPrincipal().getAccount(),
			folderkey.getId(),
			ckey.getId(),
			StringUtils.isBlank(filename)? "%": StringUtils.trim(filename) + "%"
		};
		
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		PageWrapper<CabFileInfo> pwrapper = new PageWrapper<CabFileInfo>();
		
		int totalrow = pseudodao.queryRowCount(jtemplate, SQL_COUNT_COLS.append(filterbuf).toString(), params);
		// calculate pagination information, the page menu number is 5
		PaginationInfo pagination = new PaginationHelper(totalrow, 
				pagequery.getPageNumber(), 
				pagequery.getPageSize(), 5).getPaginationInfo();
		
		pwrapper.setPagination(pagination);
		
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL_COLS.toString(), pagequery);

		StringBuffer folderbuf = new StringBuffer("SELECT f0.* FROM gp_cab_files f0,");
		folderbuf.append("(").append(pagesql).append(") f1 ")
				.append("WHERE f0.file_id = f1.file_id ");
	
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + folderbuf + " / params : " + ArrayUtils.toString(params));
		}
		
		List<CabFileInfo> result = null;
		try{
			
			result = jtemplate.query(folderbuf.toString(), params, cabfiledao.getRowMapper());
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail query files in cabinet", dae);
		}
		pwrapper.setRows(result);
		
		return pwrapper;
	}
	
	@Override
	public PageWrapper<CabEntryInfo> getCabEntries(ServiceContext<?> svcctx, 
			InfoId<Long> ckey,
			InfoId<Long> folderkey,
			String entryname, 
			PageQuery pagequery) throws ServiceException {
		
		StringBuffer SQL_COLS = new StringBuffer("SELECT distinct a.entry_id,a.entry_type,a.cabinet_id ");
		StringBuffer SQL_COUNT_COLS = new StringBuffer("SELECT count(distinct a.entry_id,a.entry_type,a.cabinet_id) ");
		
		StringBuffer filterbuf = new StringBuffer();
		
		filterbuf.append("FROM gp_cab_entries a LEFT JOIN gp_cab_ace b ON a.acl_id = b.acl_id ")
					.append("WHERE ")
					.append("( ");
		/** ( ... OR ... OR ... OR ... OR ...) AND () */
		/** set owner filter condition */
		filterbuf.append("  ( a.owner = ? AND b.subject_type = 'o' AND ")
					      .append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value).append(" ) OR ");
		/** set group filter condition*/
		if(!CollectionUtils.isEmpty(svcctx.getPrincipal().getGroups())){
			filterbuf.append("  ( b.subject_type = 'g' AND ").append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value)
						.append(" AND b.subject IN ( ").append(DAOSupport.getInClause(svcctx.getPrincipal().getGroups())).append(" )) OR ");
		}
		/** set everyone filter condition */
		filterbuf.append("  ( b.subject_type = 'e' AND ").append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value).append(" )  OR ");
		/** set user filter condition */
		filterbuf.append("  ( b.subject_type = 'u' AND ").append("b.privilege&").append(AcePrivilege.BROWSE.value).append("=").append(AcePrivilege.BROWSE.value)
					.append(" AND b.subject = ? ))");		
		filterbuf.append(" AND a.folder_pid = ? "); // folder id
		filterbuf.append(" AND a.cabinet_id = ? "); // cabinet id
		filterbuf.append(" AND a.entry_name like ? "); // folder name
		// find all records
		SQL_COLS.append(filterbuf);

		Object[] params = new Object[]{
			svcctx.getPrincipal().getAccount(),
			svcctx.getPrincipal().getAccount(),
			folderkey.getId(),
			ckey.getId(),
			StringUtils.isBlank(entryname)? "%": StringUtils.trim(entryname) + "%"
		};
		
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		PageWrapper<CabEntryInfo> pwrapper = new PageWrapper<CabEntryInfo>();
		
		int totalrow = pseudodao.queryRowCount(jtemplate, SQL_COUNT_COLS.append(filterbuf).toString(), params);
		// calculate pagination information, the page menu number is 5
		PaginationInfo pagination = new PaginationHelper(totalrow, 
				pagequery.getPageNumber(), 
				pagequery.getPageSize(), 5).getPaginationInfo();
		
		pwrapper.setPagination(pagination);
		
		// get page query sql
		String pagesql = pseudodao.getPageQuerySql(SQL_COLS.toString(), pagequery);

		StringBuffer folderbuf = new StringBuffer("SELECT f0.* FROM gp_cab_entries f0,");
		folderbuf.append("(").append(pagesql).append(") f1 ")
				.append("WHERE f0.cabinet_id = f1.cabinet_id ")
				.append("AND f0.entry_id = f1.entry_id ")
				.append("AND f0.entry_type = f1.entry_type ");
	
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + folderbuf + " / params : " + ArrayUtils.toString(params));
		}
		
		List<CabEntryInfo> result = null;
		try{
			
			result = jtemplate.query(folderbuf.toString(), params, CabEntryMapper);
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("Fail query entries in cabinet", dae);
		}
		pwrapper.setRows(result);
		
		return pwrapper;
	}
	
	@Override
	public List<CabinetInfo> getCabinets(ServiceContext<?> svcctx, String account) throws ServiceException {

		List<CabinetInfo> rtv = null;
		StringBuffer SQL = new StringBuffer();
		SQL.append("select * from gp_cabinets a, gp_users b ")
				.append("where (b.public_cabinet_id = a.cabinet_id or b.private_cabinet_id = a.cabinet_id) and ")
				.append("a.workgroup_id = ? and ")
				.append("b.account = ? ");
		
		
		JdbcTemplate jdbctemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		RowMapper<CabinetInfo> mapper = cabinetdao.getRowMapper();
		
		Object[] parms = new Object[]{GeneralConstants.PERSON_WORKGROUP, account};
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(parms));
		try{
			rtv = jdbctemplate.query(SQL.toString(), parms, mapper);
		}catch(DataAccessException dae){
			
			throw new ServiceException("fail get cabinets of account[" + account + "]",dae);
		}
		return rtv;
	}

	public static RowMapper<CabEntryInfo> CabEntryMapper = new RowMapper<CabEntryInfo>(){

		@Override
		public CabEntryInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			CabEntryInfo info = null;
			
			String isfolder = rs.getString("entry_type");
			InfoId<Long> id = null;
			if("FOLDER".equals(isfolder)){
				id = IdKey.CAB_FOLDER.getInfoId(rs.getLong("entry_id"));
				info = new CabEntryInfo(true);
			}else{
				id = IdKey.CAB_FILE.getInfoId(rs.getLong("entry_id"));
				info = new CabEntryInfo(false);
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
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};

}
