package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.GroupDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.GroupInfo;
import com.gp.info.InfoId;

@Component("groupDAO")
public class GroupDAOImpl extends DAOSupport implements GroupDAO{

	Logger LOGGER = LoggerFactory.getLogger(GroupDAOImpl.class);
	
	@Autowired
	public GroupDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( GroupInfo info) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_groups (")
			.append("manage_id,group_id,group_type,")
			.append("group_name,descr,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,?,")
			.append("?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				info.getManageId(),key.getId(),info.getGroupType(),
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
	public int update(GroupInfo info, FilterMode mode, FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("UPDATE gp_groups SET ");
		
		if(columnCheck(mode, colset, "manage_id")){
			SQL.append("manage_id = ?,");
			params.add(info.getManageId());
		}
		if(columnCheck(mode, colset, "group_type")){
			SQL.append("group_type = ?,");
			params.add(info.getGroupType());
		}
		if(columnCheck(mode, colset, "group_name")){
			SQL.append("group_name = ?,");
			params.add(info.getGroupName());
		}
		if(columnCheck(mode, colset, "descr")){
			SQL.append("descr = ? ,");
			params.add(info.getDescription());
		}
		SQL.append("modifier = ?, last_modified = ? ")
			.append("where group_id = ? ");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : "+ SQL.toString() + "/ PARAMS : " + ArrayUtils.toString(params));
		
		int rtv = jtemplate.update(SQL.toString(),params.toArray());
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

	@Override
	public int deleteByName(InfoId<Long> manageId,String type, String group) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("DELETE FROM gp_groups ")
			.append("WHERE manage_id = ? AND group_name = ? AND group_type = ?");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
				manageId.getId(),
				group,
				type
		};
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : "+ SQL.toString() + "/ PARAMS : " + ArrayUtils.toString(params));
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public GroupInfo queryByName(InfoId<Long> manageId, String type, String group) {
		
		String SQL = "SELECT * FROM gp_groups "
				+ "WHERE group_name = ? AND manage_id = ? AND group_type = ?";
		
		Object[] params = new Object[]{				
				group,
				manageId.getId(),
				type
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("SQL : "+ SQL.toString() + "/ PARAMS : " + ArrayUtils.toString(params));
		List<GroupInfo> infos = jtemplate.query(SQL, params, GroupMapper);
		
		return CollectionUtils.isEmpty(infos) ? null : infos.get(0);
	}
}
