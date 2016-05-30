package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.CabFolderDAO;
import com.gp.info.CabFolderInfo;
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
			.append("?,?,")
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
	public int update(CabFolderInfo info) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_cab_folders set ")
			.append("cabinet_id = ? ,folder_pid = ? ,source_id = ? ,")
			.append("folder_name = ? ,descr = ? ,profile = ? ,properties = ? ,")
			.append("acl_id = ? ,total_size = ? ,owner = ? ,folder_count = ? ,")
			.append("owm = ? ,state = ? ,hash_code = ? ,file_count = ? ,")
			.append("create_time = ? ,creator = ? ,classification = ?,")
			.append("modifier = ?, last_modified = ? ")
			.append("where folder_id = ? " );
		
		Object[] params = new Object[]{
				info.getCabinetId(),info.getParentId(),info.getSourceId(),
				info.getEntryName(),info.getDescription(),info.getProfile(),info.getProperties(),
				info.getAclId(),info.getTotalSize(),info.getOwner(),info.getFolderCount(),
				info.getOwm(),info.getState(),info.getHashCode(),info.getFileCount(),
				info.getCreateDate(),info.getCreator(),info.getClassification(),
				info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params);
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
