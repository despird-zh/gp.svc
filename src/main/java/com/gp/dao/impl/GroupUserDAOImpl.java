package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
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
import com.gp.dao.GroupUserDAO;
import com.gp.info.GroupUserInfo;
import com.gp.info.InfoId;

@Component("groupUserDAO")
public class GroupUserDAOImpl extends DAOSupport implements GroupUserDAO{

	Logger LOGGER = LoggerFactory.getLogger(GroupUserDAOImpl.class);
	
	@Autowired
	public GroupUserDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( GroupUserInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_group_user (")
			.append("rel_id,group_id,")
			.append("account,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,")
			.append("?,")
			.append("?,?)");
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getGroupId(),
				info.getAccount(),
				info.getModifier(),info.getModifyDate()
		};
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		return jtemplate.update(SQL.toString(),params);
		
	}

	@Override
	public int delete( InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_group_user ")
			.append("where rel_id = ? ");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public int update(GroupUserInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_group_user set ")
			.append("group_id = ?,")
			.append("account = ?,")
			.append("modifier = ?, last_modified = ? ")
			.append("where rel_id = ? ");
		
		Object[] params = new Object[]{
				info.getGroupId(),
				info.getAccount(),
				info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int rtv = jtemplate.update(SQL.toString(),params);
		return rtv;
	}

	@Override
	public GroupUserInfo query( InfoId<?> id) {
		String SQL = "select * from gp_group_user "
				+ "where rel_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		GroupUserInfo ainfo = jtemplate.queryForObject(SQL, params, GroupUserMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
	}

	public static RowMapper<GroupUserInfo> GroupUserMapper = new RowMapper<GroupUserInfo>(){

		@Override
		public GroupUserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			GroupUserInfo info = new GroupUserInfo();
			InfoId<Long> id = IdKey.GROUP_USER.getInfoId(rs.getLong("rel_id"));
			info.setInfoId(id);
			
			info.setAccount(rs.getString("account"));
			info.setGroupId(rs.getLong("group_id"));

			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};

	@Override
	public RowMapper<GroupUserInfo> getRowMapper() {
	
		return GroupUserMapper;
	}

	@Override
	public int deleteByAccount(InfoId<Long> groupId, String account) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_group_user ")
		.append("where group_id = ? and account = ?");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			groupId.getId(), account
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public InfoId<Long> existByAccount(InfoId<Long> groupId, String account) {
		String SQL = "select rel_id from gp_group_user "
				+ "where group_id = ? and account = ?";
		
		Object[] params = new Object[]{				
				groupId.getId(),
				account
			};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		List<Long> cnt = jtemplate.queryForList(SQL, params, Long.class);
		
		return CollectionUtils.isEmpty(cnt)?null:IdKey.GROUP_USER.getInfoId(cnt.get(0));
	}

	@Override
	public int deleteByGroup(InfoId<Long> groupId) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_group_user ")
		.append("where group_id = ? ");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			groupId.getId()
		};
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}
}
