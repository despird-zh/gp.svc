package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.CabFolderDAO;
import com.gp.info.CabFolderInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component("cabFolderDAO")
public class CabFolderDAOImpl extends DAOSupport implements CabFolderDAO{
	
	Logger LOGGER = LoggerFactory.getLogger(CabFolderDAOImpl.class);
	
	@Autowired
	public CabFolderDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( CabFolderInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_cab_folders (")
			.append("source_id, cabinet_id, folder_pid, folder_id,")
			.append("folder_name, descr, profile, properties,")
			.append("acl_id, total_size, owner, folder_count,")
			.append("owm, state, hash_code, file_count,")
			.append("create_time, creator,classification,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				info.getSourceId(),info.getCabinetId(),info.getParentId(),key.getId(),
				info.getEntryName(),info.getDescription(),info.getProfile(),info.getProperties(),
				info.getAclId(),info.getTotalSize(),info.getOwner(),info.getFolderCount(),
				info.getOwm(),info.getState(),info.getHashCode(),info.getFileCount(),
				info.getCreateDate(),info.getCreator(),info.getClassification(),
				info.getModifier(),info.getModifyDate()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		return jtemplate.update(SQL.toString(),params);
		
	}

	@Override
	public int delete( InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_cab_folders ")
			.append("where folder_id = ? ");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public int update(CabFolderInfo info, FlatColLocator ...exclcols) {
		Set<String> cols = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_cab_folders set ");
		if(!cols.contains("cabinet_id")){
			SQL.append("cabinet_id = ? ,");
			params.add(info.getCabinetId());
		}
		if(!cols.contains("folder_pid")){
			SQL.append("folder_pid = ? ,");
			params.add(info.getParentId());
		}
		if(!cols.contains("source_id")){
			SQL.append("source_id = ? ,");
			params.add(info.getSourceId());
		}
		if(!cols.contains("folder_name")){
			SQL.append("folder_name = ? ,");
			params.add(info.getEntryName());
		}
		if(!cols.contains("descr")){
			SQL.append("descr = ? ,");
			params.add(info.getDescription());
		}
		if(!cols.contains("profile")){
			SQL.append("profile = ? ,");
			params.add(info.getProfile());
		}
		if(!cols.contains("properties")){
			SQL.append("properties = ? ,");
			params.add(info.getProperties());
		}
		if(!cols.contains("acl_id")){
			SQL.append("acl_id = ? ,");
			params.add(info.getAclId());
		}
		if(!cols.contains("total_size")){
			SQL.append("total_size = ? ,");
			params.add(info.getTotalSize());
		}
		if(!cols.contains("owner")){
			SQL.append("owner = ? ,");
			params.add(info.getOwner());
		}
		if(!cols.contains("folder_count")){
			SQL.append("folder_count = ? ,");
			params.add(info.getFolderCount());
		}
		if(!cols.contains("owm")){
			SQL.append("owm = ? ,");
			params.add(info.getOwm());
		}
		if(!cols.contains("state")){
			SQL.append("state = ? ,");
			params.add(info.getState());
		}
		if(!cols.contains("hash_code")){
			SQL.append("hash_code = ? ,");
			params.add(info.getHashCode());
		}
		if(!cols.contains("file_count")){
			SQL.append("file_count = ? ,");
			params.add(info.getFileCount());
		}
		if(!cols.contains("create_time")){
			SQL.append("create_time = ? ,");
			params.add(info.getCreateDate());
		}
		if(!cols.contains("creator")){
			SQL.append("creator = ? ,");
			params.add(info.getCreator());
		}
		if(!cols.contains("classification")){
			SQL.append("classification = ?,");
			params.add(info.getClassification());
		}

		SQL.append("modifier = ?, last_modified = ? ")
			.append("where folder_id = ? " );
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
	
	
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params.toArray());
		return rtv;
	}

	@Override
	public CabFolderInfo query( InfoId<?> id) {
		String SQL = "select * from gp_cab_folders "
				+ "where folder_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<CabFolderInfo> ainfo = jtemplate.query(SQL, params, CabFolderMapper);
		return ainfo.size()>0? ainfo.get(0):null;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<CabFolderInfo> CabFolderMapper = new RowMapper<CabFolderInfo>(){

		@Override
		public CabFolderInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			CabFolderInfo info = new CabFolderInfo();
			InfoId<Long> id = IdKey.CAB_FOLDER.getInfoId(rs.getLong("folder_id"));
			info.setInfoId(id);

			info.setSourceId(rs.getInt("source_id"));
			info.setCabinetId(rs.getLong("cabinet_id"));
			info.setParentId(rs.getLong("folder_pid"));
			info.setEntryName(rs.getString("folder_name"));
			info.setDescription(rs.getString("descr"));
			info.setProfile(rs.getString("profile"));
			info.setProperties(rs.getString("properties"));
			info.setAclId(rs.getLong("acl_id"));
			info.setTotalSize(rs.getLong("total_size"));
			info.setOwner(rs.getString("owner"));
			info.setFolderCount(rs.getInt("folder_count"));
			info.setFileCount(rs.getInt("file_count"));
			info.setState(rs.getString("state"));
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

	@Override
	public RowMapper<CabFolderInfo> getRowMapper() {
	
		return CabFolderMapper;
	}

	@Override
	public CabFolderInfo queryByName(InfoId<Long> parentKey, String foldername) {
		String SQL = "Select * from gp_cab_folders where folder_name = ? and folder_pid = ?";
		
		Object[] params = new Object[]{				
				foldername,
				parentKey.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		List<CabFolderInfo> ainfo = jtemplate.query(SQL, params, CabFolderMapper);
		return ainfo.size()>0 ? ainfo.get(0):null;
	}

	@Override
	public List<CabFolderInfo> queryByParent(Long parentFolderId) {
		
		String SQL = "select * from gp_cab_folders "
				+ "where folder_pid = ?";
		
		Object[] params = new Object[]{				
				parentFolderId
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		List<CabFolderInfo> ainfo = jtemplate.query(SQL, params, CabFolderMapper);
		return ainfo;
	}

}
