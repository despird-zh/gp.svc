package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.UserDAO;
import com.gp.info.InfoId;
import com.gp.info.UserExInfo;
import com.gp.info.UserInfo;

@Component("userDAO")
public class UserDAOImpl extends DAOSupport implements UserDAO{

	Logger LOGGER = LoggerFactory.getLogger(UserDAOImpl.class);
	
	@Autowired
	public UserDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
		
	@Override
	public int create( UserInfo info) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_users (")
			.append("source_id,user_id,account,global_account,")
			.append("type,mobile,phone,full_name,")
			.append("email, password, state, create_time,")
			.append("extra_info, retry_times, last_logon,classification,")
			.append("language, timezone, publish_cabinet_id, netdisk_cabinet_id,")
			.append("storage_id,modifier,last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				info.getSourceId(),key.getId(),info.getAccount(),info.getGlobalAccount(),
				info.getType(),info.getMobile(),info.getPhone(),info.getFullName(),
				info.getEmail(),info.getPassword(),info.getState(),info.getCreateDate(),
				info.getExtraInfo(),info.getRetryTimes(),info.getLastLogonDate(),info.getClassification(),
				info.getLanguage(), info.getTimeZone(),info.getPublishCabinet(),info.getNetdiskCabinet(),
				info.getStorageId(),info.getModifier(),info.getModifyDate()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);

			// execute sql
		return jtemplate.update(SQL.toString(),params);

	}

	@Override
	public int delete( InfoId<?> id) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_users ")
			.append("where user_id = ? ");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = -1;

			rtv = jtemplate.update(SQL.toString(), params);

		return rtv;
	}

	@Override
	public int update( UserInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_users set ")
		.append("account = ?,global_account = ?,source_id = ? ,")
		.append("type = ?,mobile = ?,phone = ?,full_name = ?,")
		.append("email = ?, password = ?, state = ?, create_time = ?,")
		.append("extra_info = ?, retry_times = ?, last_logon = ?,classsification=?,")
		.append("language = ?, timezone = ?, publish_cabinet_id = ?, netdisk_cabinet_id = ?,")
		.append("storage_id = ?,modifier = ?,last_modified = ? ")
		.append("where user_id = ? ");

		Object[] params = new Object[]{
				info.getAccount(),info.getGlobalAccount(),info.getSourceId(),
				info.getType(),info.getMobile(),info.getPhone(),info.getFullName(),
				info.getEmail(),info.getPassword(),info.getState(),info.getCreateDate(),
				info.getExtraInfo(),info.getRetryTimes(),info.getLastLogonDate(),info.getClassification(),
				info.getLanguage(), info.getTimeZone(),info.getPublishCabinet(),info.getNetdiskCabinet(),
				info.getStorageId(),info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int rtv = -1;

		rtv = jtemplate.update(SQL.toString(), params);

		return rtv;
	}

	@Override
	public UserInfo query( InfoId<?> id) {
		String SQL = "select * from gp_users "
				+ "where user_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);

		List<UserInfo>	ainfo = jtemplate.query(SQL, params, UserMapper);

		return CollectionUtils.isEmpty(ainfo) ? null : ainfo.get(0);
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<UserInfo> UserMapper = new RowMapper<UserInfo>(){

		@Override
		public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserInfo info = new UserInfo();
			InfoId<Long> id = IdKey.USER.getInfoId(rs.getLong("user_id"));
			info.setInfoId(id);

			info.setSourceId(rs.getInt("source_id"));
			info.setAccount(rs.getString("account"));
			info.setType(rs.getString("type"));
			info.setMobile(rs.getString("mobile"));
			info.setPhone(rs.getString("phone"));
			info.setFullName(rs.getString("full_name"));
			info.setEmail(rs.getString("email"));
			info.setPassword(rs.getString("password"));
			info.setState(rs.getString("state"));
			info.setCreateDate(rs.getTimestamp("create_time"));
			info.setExtraInfo(rs.getString("extra_info"));
			info.setRetryTimes(rs.getInt("retry_times"));
			info.setLastLogonDate(rs.getDate("last_logon"));
			info.setLanguage(rs.getString("language"));
			info.setTimeZone(rs.getString("timezone"));
			info.setPublishCabinet(rs.getLong("publish_cabinet_id"));
			info.setNetdiskCabinet(rs.getLong("netdisk_cabinet_id"));
			info.setGlobalAccount(rs.getString("global_account"));
			info.setStorageId(rs.getInt("storage_id"));
			info.setClassification(rs.getString("classification"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}};

	@Override
	public boolean existAccount( String account) {
		
		String SQL = "select count(user_id) as account_count from gp_users "
				+ "where account = ? ";
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString());
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		
		Integer count = jtemplate.queryForObject(SQL, Integer.class, account);
		
		return count > 0;
	}

	@Override
	public UserInfo queryByAccount( String account) {
		String SQL = "select * from gp_users "
				+ "where account = ?";
		
		Object[] params = new Object[]{				
				account
			};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		UserInfo ainfo = jtemplate.queryForObject(SQL, params, UserMapper);
		return ainfo;
	}

	@Override
	public int changePassword( String account, String password) {
		String SQL = "update gp_users set password = ? where account = ?";
		
		Object[] params = new Object[]{
				password,
				account
			};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int rtv = -1;

			rtv = jtemplate.update(SQL.toString(), params);

		return rtv;
	}
	
	@Override
	public List<UserInfo> queryAccounts( String accountname, String instance, String[] type)
			{
		
		List<UserInfo> rtv = null;
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_users where 1=1 ");
		List<Object> parmlist = new ArrayList<Object>();
		// account or name condition
		if(StringUtils.isNotBlank(accountname)){
			
			SQL.append(" and (account like ? or full_name like ? ) ");
			parmlist.add("%" + StringUtils.trim(accountname) + "%");
			parmlist.add("%" + StringUtils.trim(accountname) + "%");
		}
		// entity condition
		if(StringUtils.isNotBlank(instance)){
			
			SQL.append(" and source_id = ? ");
			parmlist.add(instance);
		}
		// user type condition
		if(!ArrayUtils.isEmpty(type)){
			SQL.append(" and type in ("); 
			for (int i=0; i< type.length; i++) { 
				if (i!=0) SQL.append(", "); 
				SQL.append("?"); 
				parmlist.add(type[i]);
			} 
			SQL.append(")"); 
		}
		
		Object[] params = parmlist.toArray();
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
			
		rtv = jtemplate.query(SQL.toString(), params, UserMapper);
		return rtv;
			
	}

	@Override
	public RowMapper<UserInfo> getRowMapper() {
		
		return UserMapper;
	}

	@Override
	public int updateAsNeed(UserInfo info) {
		StringBuffer SQL = new StringBuffer();
		
		SQL.append("update gp_users set ")
		.append("type = ?,mobile = ?, phone = ?,full_name = ?,")
		.append("email = ?, password = ?, state = ?, ")
		.append("language = ?, timezone = ?,")
		.append("storage_id = ?, modifier = ?,last_modified = ? ")
		.append("where user_id = ? ");

		Object[] params = new Object[]{
				info.getType(),info.getMobile(),info.getPhone(),info.getFullName(),
				info.getEmail(),info.getPassword(),info.getState(),				
				info.getLanguage(), info.getTimeZone(),
				info.getStorageId(),info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int rtv = -1;

			rtv = jtemplate.update(SQL.toString(), params);

		return rtv;
	}
	
}
