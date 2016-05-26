package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

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
import com.gp.dao.AttachRelDAO;
import com.gp.info.AttachRelInfo;
import com.gp.info.InfoId;

@Component("attachRelDAO")
public class AttachRelDAOImpl extends DAOSupport implements AttachRelDAO{
	
	Logger LOGGER = LoggerFactory.getLogger(AttachRelDAOImpl.class);
	
	@Autowired
	public AttachRelDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( AttachRelInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_attach_rel (")
			.append("rel_id, workgroup_id,")
			.append("resource_id,resource_type,atta_id,atta_name,")
			.append("atta_type,modifier, last_modified")
			.append(")values(")
			.append("?,?,")
			.append("?,?,?,?,")
			.append("?,?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getWorkgroupId(),
				info.getResourceId(),info.getResourceType(),info.getAttachId(),info.getAttachName(),
				info.getAttachType(),info.getModifier(),info.getModifyDate()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int cnt = jtemplate.update(SQL.toString(),params);
		return cnt;
	}

	@Override
	public int delete( InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_attach_rel ")
			.append("where rel_id = ?");
		
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
	public int update( AttachRelInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_attach_rel set ")
			.append("workgroup_id = ?,resource_id =?,")
			.append("resource_type = ?,atta_id = ?, atta_name = ?, atta_type=?,")
			.append("modifier = ?, last_modified = ? ")
			.append("where rel_id = ? ");
		
		Object[] params = new Object[]{
				info.getWorkgroupId(),info.getResourceId(),
				info.getResourceType(),info.getAttachId(),info.getAttachName(),info.getAttachType(),
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
	public AttachRelInfo query( InfoId<?> id) {
		String SQL = "select * from gp_attach_rel "
				+ "where rel_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		AttachRelInfo ainfo = jtemplate.queryForObject(SQL, params, AttachRelMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<AttachRelInfo> AttachRelMapper = new RowMapper<AttachRelInfo>(){

		@Override
		public AttachRelInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			
			AttachRelInfo info = new AttachRelInfo();
			InfoId<Long> id = IdKey.ATTACH_REL.getInfoId(rs.getLong("rel_id"));
			
			info.setInfoId(id);

			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setAttachId(rs.getLong("atta_id"));
			info.setAttachName(rs.getString("atta_name"));
			info.setAttachType(rs.getString("atta_type"));
			info.setResourceId(rs.getLong("resource_id"));
			info.setResourceType(rs.getString("resource_type"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
		
	};
		
	@Override
	public RowMapper<AttachRelInfo> getRowMapper() {
		
		return AttachRelMapper;
	}
}
