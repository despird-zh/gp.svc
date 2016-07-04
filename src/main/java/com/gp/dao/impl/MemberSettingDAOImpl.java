package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.gp.dao.MemberSettingDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.MemberSettingInfo;

@Component("memberSettingDAO")
public class MemberSettingDAOImpl extends DAOSupport implements MemberSettingDAO{

	static Logger LOGGER = LoggerFactory.getLogger(InstanceDAOImpl.class);
	
	@Autowired
	public MemberSettingDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(MemberSettingInfo info) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("INSERT INTO gp_member_setting (")
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
		SQL.append("delete from gp_member_setting ")
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
	public int update(MemberSettingInfo info, FlatColLocator... excludeCols) {
		Set<String> cols = FlatColumns.toColumnSet(excludeCols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_member_setting set ");
		
		if(!cols.contains("manage_id")){
			SQL.append("manage_id = ?,");
			params.add(info.getManageId());
		}
		if(!cols.contains("account")){
			SQL.append("account = ?,");
			params.add(info.getAccount());
		}
		if(!cols.contains("group_type")){
			SQL.append("group_type = ?,");
			params.add(info.getGroupType());
		}
		if(!cols.contains("post_visible")){
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
		String SQL = "select * from gp_member_setting "
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
	public RowMapper<MemberSettingInfo> getRowMapper() {
		
		return MemberSettingMapper;
	}

	static RowMapper<MemberSettingInfo> MemberSettingMapper = new RowMapper<MemberSettingInfo>(){

		@Override
		public MemberSettingInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			MemberSettingInfo info = new MemberSettingInfo();
			InfoId<Long> id = IdKey.MBR_SETTING.getInfoId(rs.getLong("rel_id"));
			info.setInfoId(id);
			
			info.setAccount(rs.getString("account"));
			info.setGroupType(rs.getString("group_type"));
			info.setManageId(rs.getLong("manage_id"));
			info.setPostVisible(rs.getBoolean("post_visible"));
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
		
	};
	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {

		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
