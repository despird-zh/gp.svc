package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.dao.CabVersionDAO;
import com.gp.info.CabVersionInfo;
import com.gp.info.InfoId;

@Component("cabVersionDAO")
public class CabVersionDAOImpl extends DAOSupport implements CabVersionDAO{

	Logger LOGGER = LoggerFactory.getLogger(CabVersionDAOImpl.class);
	
	@Autowired
	public CabVersionDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( CabVersionInfo info) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_cab_versions (")
			.append("source_id,cabinet_id,folder_id,file_id,version_id,")
			.append("file_name,descr,profile,properties,owm,")
			.append("version_label,size,owner,comment_on,")
			.append("version,state,binary_id,format,")
			.append("create_time,creator,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,?,?,?,")
			.append("?,?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		
		Object[] params = new Object[]{
				info.getSourceId(),info.getCabinetId(),info.getParentId(),info.getFileId(),key.getId(),
				info.getFileName(),info.getDescription(),info.getProfile(),info.getProperties(),info.getOwm(),
				info.getVersionLabel(),info.getSize(),info.getOwner(),info.isCommentOn(),
				info.getVersion(),info.getState(),info.getBinaryId(),info.getFormat(),
				info.getCreateDate(),info.getCreator(),
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
		SQL.append("delete from gp_cab_versions ")
			.append("where version_id = ? ");
		
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
	public int update(CabVersionInfo info) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_cab_versions set ")
			.append("cabinet_id = ? ,folder_id = ? ,source_id = ? ,")
			.append("file_name = ? ,descr = ? ,profile = ? ,properties = ? ,")
			.append("version_label = ? ,size = ? ,owner = ? ,comment_on = ? ,")
			.append("version = ? ,state = ? ,binary_id = ? ,format = ? ,")
			.append("create_time = ? ,creator = ? ,file_id = ?, owm=?,")
			.append("modifier = ?, last_modified = ? ")
			.append("where version_id = ? ");
		
		Object[] params = new Object[]{
				info.getCabinetId(),info.getParentId(),info.getSourceId(),
				info.getFileName(),info.getDescription(),info.getProfile(),info.getProperties(),
				info.getVersionLabel(),info.getSize(),info.getOwner(),info.isCommentOn(),
				info.getVersion(),info.getState(),info.getBinaryId(),info.getFormat(),
				info.getCreateDate(),info.getCreator(),info.getFileId(),info.getOwm(),
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
	public CabVersionInfo query( InfoId<?> id) {
		String SQL = "select * from gp_cab_versions "
				+ "where version_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<CabVersionInfo> ainfo = jtemplate.query(SQL, params, CabVersionMapper);
		return ainfo.size()>0 ? ainfo.get(0):null;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<CabVersionInfo> CabVersionMapper = new RowMapper<CabVersionInfo>(){

		@Override
		public CabVersionInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			CabVersionInfo info = new CabVersionInfo();
			InfoId<Long> id = IdKey.CAB_VERSION.getInfoId(	rs.getLong("version_id"));
			info.setInfoId(id);
			info.setSourceId(rs.getInt("source_id"));
			info.setCabinetId(rs.getLong("cabinet_id"));
			info.setParentId(rs.getLong("folder_id"));
			info.setFileId(rs.getLong("file_id"));
			info.setFileName(rs.getString("file_name"));
			info.setDescription(rs.getString("descr"));
			info.setProfile(rs.getString("profile"));
			info.setProperties(rs.getString("properties"));
			info.setVersionLabel(rs.getString("version_label"));
			info.setSize(rs.getLong("size"));
			info.setOwner(rs.getString("owner"));
			info.setCommentOn(rs.getBoolean("comment_on"));
			info.setVersion(rs.getString("version"));
			info.setState(rs.getString("state"));
			info.setBinaryId(rs.getLong("binary_id"));
			info.setFormat(rs.getString("format"));
			info.setOwm(rs.getLong("owm"));
			info.setCreator(rs.getString("creator"));
			info.setCreateDate(rs.getTimestamp("create_time"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};

	@Override
	public RowMapper<CabVersionInfo> getRowMapper() {
		
		return CabVersionMapper;
	}

	@Override
	public List<CabVersionInfo> queryByFileId(Long fileid) {
		
		StringBuffer SQL_BUF = new StringBuffer("Select * from gp_cab_versions where file_id = ?");
		
		Object[] params = new Object[]{				
				fileid
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		List<CabVersionInfo> ainfo = jtemplate.query(SQL_BUF.toString(), params, CabVersionMapper);
		return ainfo;
	}
}
