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
import com.gp.config.ServiceConfigurator;
import com.gp.dao.AttachDAO;
import com.gp.info.AttachInfo;
import com.gp.info.InfoId;

@Component("attachDAO")
public class AttachDAOImpl extends DAOSupport implements AttachDAO{

	Logger LOGGER = LoggerFactory.getLogger(AttachDAOImpl.class);
	
	@Autowired
	public AttachDAOImpl(@Qualifier(ServiceConfigurator.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(AttachInfo info){

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_attachments (")
			.append("attachment_id, source_id,workgroup_id,")
			.append("attachment_name,size,owner,state,")
			.append("binary_id,format,modifier, last_modified,")
			.append("hash_code,owm,create_time,creator")
			.append(")values(")
			.append("?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?)");
		
		InfoId<Long> key = info.getInfoId();
		
		Object[] params = new Object[]{
				key.getId(),info.getSourceId(),info.getWorkgroupId(),
				info.getAttachName(),info.getSize(),info.getOwner(),info.getState(),
				info.getBinaryId(),info.getFormat(),info.getModifier(),info.getModifyDate(),
				info.getHashCode(),info.getOwm(),info.getCreateDate(),info.getCreator()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int cnt = jtemplate.update(SQL.toString(),params);
		return cnt;
	}

	@Override
	public int delete(InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_attachments ")
			.append("where attachment_id = ? ");
		
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
	public int update(AttachInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_attachments set ")
			.append("workgroup_id = ?,attachment_name =?,source_id = ? ,")
			.append("size = ?,owner = ? , state = ?,binary_id=?,")
			.append("hash_code= ?, owm = ?, format = ?, creator=?, create_time=? ,")
			.append("modifier = ?, last_modified = ? ")
			.append("where attachment_id = ? ");
		
		Object[] params = new Object[]{
				info.getWorkgroupId(),info.getAttachName(),info.getSourceId(),
				info.getSize(),info.getOwner(),info.getState(),info.getBinaryId(),
				info.getHashCode(), info.getOwm(),info.getFormat(),info.getCreator(),info.getCreateDate(),
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
	public AttachInfo query( InfoId<?> id) {
		String SQL = "select * from gp_attachments "
				+ "where attachment_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<AttachInfo> ainfo = jtemplate.query(SQL, params, AttachMapper);
		return ainfo.size()>0 ? ainfo.get(0):null;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public static RowMapper<AttachInfo> AttachMapper = new RowMapper<AttachInfo>(){

		@Override
		public AttachInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			AttachInfo info = new AttachInfo();
			
			InfoId<Long> id = IdKey.ATTACHMENT.getInfoId(rs.getLong("attachment_id"));
			
			info.setInfoId(id);
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setAttachName(rs.getString("attachment_name"));
			info.setSize(rs.getLong("size"));
			info.setOwner(rs.getString("owner"));
			info.setState(rs.getString("state"));
			info.setBinaryId(rs.getLong("binary_id"));
			info.setHashCode(rs.getString("hash_code"));
			info.setOwm(rs.getLong("owm"));
			info.setFormat(rs.getString("format"));
			info.setCreator(rs.getString("creator"));
			info.setCreateDate(rs.getTimestamp("create_time"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}};

	@Override
	public RowMapper<AttachInfo> getRowMapper() {
		
		return AttachMapper;
	}	
}
