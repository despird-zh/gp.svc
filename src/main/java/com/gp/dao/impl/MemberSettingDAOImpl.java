package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.gp.common.GroupUsers;
import com.gp.common.IdKey;
import com.gp.common.DataSourceHolder;
import com.gp.dao.MemberSettingDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.MemberSettingInfo;

@Component
public class MemberSettingDAOImpl extends DAOSupport implements MemberSettingDAO{

	static Logger LOGGER = LoggerFactory.getLogger(SourceDAOImpl.class);
	
	@Autowired
	public MemberSettingDAOImpl(@Qualifier(DataSourceHolder.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(MemberSettingInfo info) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("INSERT INTO gp_mbr_setting (")
			.append("rel_id, manage_id, account, group_type,")
			.append("post_visible, modifier, last_modified")
			.append(")VALUES(")
			.append("?,?,?,?,")
			.append("?,?,?")
			.append(")");
		
		Object[] params = new Object[]{
				info.getInfoId().getId(), info.getManageId(), info.getAccount(), info.getGroupType(),
				info.getPostVisible(), info.getModifier(), info.getModifyDate()
		};

		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		
		return jtemplate.update(SQL.toString(),params);
	}

	@Override
	public int delete(InfoId<?> id) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_mbr_setting ")
			.append("where rel_id = ?");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public int update(MemberSettingInfo info, FilterMode mode, FlatColLocator... excludeCols) {
		Set<String> colset = FlatColumns.toColumnSet(excludeCols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_mbr_setting set ");
		
		if(columnCheck(mode, colset, "manage_id")){
			SQL.append("manage_id = ?,");
			params.add(info.getManageId());
		}
		if(columnCheck(mode, colset, "account")){
			SQL.append("account = ?,");
			params.add(info.getAccount());
		}
		if(columnCheck(mode, colset, "group_type")){
			SQL.append("group_type = ?,");
			params.add(info.getGroupType());
		}
		if(columnCheck(mode, colset, "post_visible")){
			SQL.append("post_visible = ?,");
			params.add(info.getPostVisible());
		}
		
	
		SQL.append("modifier = ?, last_modified = ? ")
			.append("where rel_id = ?");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, params);
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int rtv = jtemplate.update(SQL.toString(),params.toArray());
		return rtv;
	}

	@Override
	public MemberSettingInfo query(InfoId<?> id) {
		String SQL = "select * from gp_mbr_setting "
				+ "where rel_id = ?";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		MemberSettingInfo ainfo = jtemplate.queryForObject(SQL, params, MemberSettingMapper);
		return ainfo;
	}
	
	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {

		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public MemberSettingInfo queryByMember(InfoId<Long> manageId, String account) {
		String SQL = "SELECT * FROM gp_mbr_setting WHERE manage_id = ? AND group_type = ? AND account = ?";
		String type = null;
		if(IdKey.GP_ORG_HIER.getSchema().equals(manageId.getIdKey()))
			type = GroupUsers.GroupType.ORG_HIER_MBR.name();
		else if(IdKey.GP_WORKGROUPS.getSchema().equals(manageId.getIdKey()))
			type = GroupUsers.GroupType.WORKGROUP_MBR.name();
		
		Object[] params = new Object[]{
			manageId.getId(),
			type,
			account
		};
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}", SQL, Arrays.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		List<MemberSettingInfo> infos = jtemplate.query(SQL, params, MemberSettingMapper);
		return CollectionUtils.isEmpty(infos)? null : infos.get(0);
	}

}
