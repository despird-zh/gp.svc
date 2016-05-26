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
import com.gp.dao.CabFileDAO;
import com.gp.info.CabFileInfo;
import com.gp.info.InfoId;

@Component("cabFileDAO")
public class CabFileDAOImpl extends DAOSupport implements CabFileDAO{
	
	Logger LOGGER = LoggerFactory.getLogger(CabFileDAOImpl.class);
	
	@Autowired
	public CabFileDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( CabFileInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_cab_files (")
			.append("source_id,cabinet_id,folder_id,file_id,")
			.append("file_name,descr,profile,properties,hash_code,")
			.append("acl_id,size,owner,comment_on,owm,")
			.append("version,version_label,state,binary_id,format,")
			.append("create_time,creator,classification,")
			.append("modifier,last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?,?,")
			.append("?,?,?,?,?,")
			.append("?,?,?,?,?,")
			.append("?,?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				info.getSourceId(),info.getCabinetId(),info.getParentId(),key.getId(),
				info.getEntryName(),info.getDescription(),info.getProfile(),info.getProperties(),info.getHashCode(),
				info.getAclId(),info.getSize(),info.getOwner(),info.isCommentOn(),info.getOwm(),
				info.getVersion(),info.getVersionLabel(),info.getState(),info.getBinaryId(),info.getFormat(),
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
		SQL.append("delete from gp_cab_files ")
			.append("where file_id = ? ");
		
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
	public int update(CabFileInfo info) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_cab_files set ")
			.append("cabinet_id = ? ,folder_id = ? ,source_id = ? ,")
			.append("file_name = ? ,descr = ? ,profile = ? ,properties = ? ,hash_code=?,")
			.append("acl_id = ? ,size = ? ,owner = ? ,comment_on = ? ,owm = ?,")
			.append("version = ? ,version_label=?,state = ? ,binary_id = ? ,format = ? ,")
			.append("create_time = ? ,creator = ? ,classification = ?,")
			.append("modifier = ?, last_modified = ? ")
			.append("where file_id = ? ");
		
		Object[] params = new Object[]{
				info.getCabinetId(),info.getParentId(),info.getSourceId(),
				info.getEntryName(),info.getDescription(),info.getProfile(),info.getProperties(),info.getHashCode(),
				info.getAclId(),info.getSize(),info.getOwner(),info.isCommentOn(),info.getOwm(),
				info.getVersion(),info.getVersionLabel(),info.getState(),info.getBinaryId(),info.getFormat(),
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
	public CabFileInfo query( InfoId<?> id) {
		String SQL = "select * from gp_cab_files "
				+ "where file_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<CabFileInfo> ainfo = jtemplate.query(SQL, params, CabFileMapper);
		return ainfo.size()>0 ? ainfo.get(0):null;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<CabFileInfo> CabFileMapper = new RowMapper<CabFileInfo>(){

		@Override
		public CabFileInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			CabFileInfo info = new CabFileInfo();
			InfoId<Long> id = IdKey.CAB_FILE.getInfoId(rs.getLong("file_id"));
			info.setInfoId(id);
	
			info.setSourceId(rs.getInt("source_id"));
			info.setCabinetId(rs.getLong("cabinet_id"));
			info.setParentId(rs.getLong("folder_id"));
			info.setEntryName(rs.getString("file_name"));
			info.setDescription(rs.getString("descr"));
			info.setProfile(rs.getString("profile"));
			info.setProperties(rs.getString("properties"));
			info.setAclId(rs.getLong("acl_id"));
			info.setSize(rs.getLong("size"));
			info.setOwner(rs.getString("owner"));
			info.setCommentOn(rs.getBoolean("comment_on"));
			info.setVersion(rs.getString("version"));
			info.setState(rs.getString("state"));
			info.setBinaryId(rs.getLong("binary_id"));
			info.setFormat(rs.getString("format"));
			info.setCreator(rs.getString("creator"));
			info.setCreateDate(rs.getTimestamp("create_time"));
			info.setHashCode(rs.getString("hash_code"));
			info.setClassification(rs.getString("classification"));
			info.setOwm(rs.getLong("owm"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};


	@Override
	public RowMapper<CabFileInfo> getRowMapper() {
		
		return CabFileMapper;
	}

	@Override
	public List<CabFileInfo> queryByParent(Long parentFolderId) {
		
		String SQL = "select * from gp_cab_files "
				+ "where folder_id = ?";
		
		Object[] params = new Object[]{				
				parentFolderId
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		List<CabFileInfo> ainfo = jtemplate.query(SQL, params, CabFileMapper);
		return ainfo;
	}
}
