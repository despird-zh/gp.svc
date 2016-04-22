package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.dao.GroupDAO;
import com.gp.info.GroupInfo;
import com.gp.info.InfoId;

@Component("groupDAO")
public class GroupDAOImpl extends DAOSupport implements GroupDAO{

	Logger LOGGER = LoggerFactory.getLogger(GroupDAOImpl.class);
	
	@Autowired
	public GroupDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( GroupInfo info) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_groups (")
			.append("workgroup_id,group_id,")
			.append("group_name,descr,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,")
			.append("?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				info.getWorkgroupId(),key.getId(),
				info.getGroupName(),info.getDescription(),
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
		SQL.append("delete from gp_groups ")
			.append("where group_id = ? ");
		
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
	public int update(GroupInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_groups set ")
			.append("workgroup_id = ?,")
			.append("group_name = ?,descr = ? ,")
			.append("modifier = ?, last_modified = ? ")
			.append("where group_id = ? ");
		
		Object[] params = new Object[]{
				info.getWorkgroupId(),
				info.getGroupName(),info.getDescription(),
				info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : "+ SQL.toString() + "/ PARAMS : " + ArrayUtils.toString(params));
		
		int rtv = jtemplate.update(SQL.toString(),params);
		return rtv;
	}

	@Override
	public GroupInfo query( InfoId<?> id) {
		String SQL = "SELECT * FROM gp_groups "
				+ "WHERE group_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : "+ SQL.toString() + "/ PARAMS : " + ArrayUtils.toString(params));
		GroupInfo ainfo = jtemplate.queryForObject(SQL, params, GroupMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
	}

	public static RowMapper<GroupInfo> GroupMapper = new RowMapper<GroupInfo>(){

		@Override
		public GroupInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			GroupInfo info = new GroupInfo();
			InfoId<Long> id = IdKey.GROUP.getInfoId(rs.getLong("group_id"));
			
			info.setInfoId(id);

			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setGroupName(rs.getString("group_name"));
			info.setDescription(rs.getString("descr"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
	};

	@Override
	public RowMapper<GroupInfo> getRowMapper() {
		
		return GroupMapper;
	}

	@Override
	public int deleteByName(Long workgroupId, String group) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("DELETE FROM gp_groups ")
			.append("WHERE workgroup_id = ? AND group_name = ?");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
				workgroupId,
				group
		};
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : "+ SQL.toString() + "/ PARAMS : " + ArrayUtils.toString(params));
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public GroupInfo queryByName(Long workgroupId, String group) {
		
		String SQL = "SELECT * FROM gp_groups "
				+ "WHERE group_name = ? AND workgroup_id = ?";
		
		Object[] params = new Object[]{				
				group,
				workgroupId,
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : "+ SQL.toString() + "/ PARAMS : " + ArrayUtils.toString(params));
		GroupInfo ainfo = jtemplate.queryForObject(SQL, params, GroupMapper);
		return ainfo;
	}
}
