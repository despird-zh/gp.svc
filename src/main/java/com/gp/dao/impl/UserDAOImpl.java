package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.common.DataSourceHolder;
import com.gp.dao.UserDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.UserInfo;

@Component
public class UserDAOImpl extends DAOSupport implements UserDAO{

	Logger LOGGER = LoggerFactory.getLogger(UserDAOImpl.class);
	
	@Autowired
	public UserDAOImpl(@Qualifier(DataSourceHolder.DATA_SRC)DataSource dataSource) {
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
			.append("signature,storage_id,avatar_id,modifier,last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				info.getSourceId(),key.getId(),info.getAccount(),info.getGlobalAccount(),
				info.getType(),info.getMobile(),info.getPhone(),info.getFullName(),
				info.getEmail(),info.getPassword(),info.getState(),info.getCreateDate(),
				info.getExtraInfo(),info.getRetryTimes(),info.getLastLogonDate(),info.getClassification(),
				info.getLanguage(), info.getTimezone(),info.getPublishCabinet(),info.getNetdiskCabinet(),
				info.getSignature(),info.getStorageId(),info.getAvatarId(),info.getModifier(),info.getModifyDate()
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
	public int update( UserInfo info, FilterMode mode, FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();

		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_users set ");
		
		if(columnCheck(mode, colset, "account")){
			SQL.append("account = ?,");
			params.add(info.getAccount());
		}
		if(columnCheck(mode, colset, "global_account")){
			SQL.append("global_account = ?,");
			params.add(info.getGlobalAccount());
		}
		if(columnCheck(mode, colset, "source_id")){
			SQL.append("source_id = ? ,");
			params.add(info.getSourceId());
		}
		if(columnCheck(mode, colset, "type")){
			SQL.append("type = ?,");
			params.add(info.getType());
		}
		if(columnCheck(mode, colset, "mobile")){
			SQL.append("mobile = ?,");
			params.add(info.getMobile());
		}
		if(columnCheck(mode, colset, "phone")){
			SQL.append("phone = ?,");
			params.add(info.getPhone());
		}
		if(columnCheck(mode, colset, "full_name")){
			SQL.append("full_name = ?,");
			params.add(info.getFullName());
		}
		if(columnCheck(mode, colset, "email")){
			SQL.append("email = ?, ");
			params.add(info.getEmail());
		}
		if(columnCheck(mode, colset, "password")){
			SQL.append("password = ?, ");
			params.add(info.getPassword());
		}
		if(columnCheck(mode, colset, "state")){
			SQL.append("state = ?, ");
			params.add(info.getState());
		}
		if(columnCheck(mode, colset, "create_time")){
			SQL.append("create_time = ?,");
			params.add(info.getCreateDate());
		}
		if(columnCheck(mode, colset, "extra_info")){
			SQL.append("extra_info = ?, ");
			params.add(info.getExtraInfo());
		}
		if(columnCheck(mode, colset, "retry_times")){
			SQL.append("retry_times = ?, ");
			params.add(info.getRetryTimes());
		}
		if(columnCheck(mode, colset, "last_logon")){
			SQL.append("last_logon = ?,");
			params.add(info.getLastLogonDate());
		}
		if(columnCheck(mode, colset, "classsification")){
			SQL.append("classsification=?,");
			params.add(info.getClassification());
		}
		if(columnCheck(mode, colset, "language")){
			SQL.append("language = ?, ");
			params.add(info.getLanguage());
		}
		if(columnCheck(mode, colset, "timezone")){
			SQL.append("timezone = ?, ");
			params.add(info.getTimezone());
		}
		if(columnCheck(mode, colset, "publish_cabinet_id")){
			SQL.append("publish_cabinet_id = ?, ");
			params.add(info.getPublishCabinet());
		}
		if(columnCheck(mode, colset, "netdisk_cabinet_id")){
			SQL.append("netdisk_cabinet_id = ?,");
			params.add(info.getNetdiskCabinet());
		}
		if(columnCheck(mode, colset, "storage_id")){
			SQL.append("storage_id = ?,");
			params.add(info.getStorageId());
		}
		if(columnCheck(mode, colset, "avatar_id")){
			SQL.append("avatar_id = ?,");
			params.add(info.getAvatarId());
		}
		if(columnCheck(mode, colset, "signature")){
			SQL.append("signature = ?,");
			params.add(info.getSignature());
		}
		SQL.append("modifier = ?,last_modified = ? ")
		.append("where user_id = ? ");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int rtv = -1;

		rtv = jtemplate.update(SQL.toString(), params.toArray());

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
		List<UserInfo> infos = jtemplate.query(SQL, params, UserMapper);
		return CollectionUtils.isEmpty(infos) ? null : infos.get(0);
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
			
			SQL.append(" AND (account like ? or full_name like ? ) ");
			parmlist.add("%" + StringUtils.trim(accountname) + "%");
			parmlist.add("%" + StringUtils.trim(accountname) + "%");
		}
		// entity condition
		if(StringUtils.isNotBlank(instance)){
			
			SQL.append(" AND source_id = ? ");
			parmlist.add(instance);
		}
		// user type condition
		if(!ArrayUtils.isEmpty(type)){
			SQL.append(" AND type in ("); 
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


	public int updateAsNeed1(UserInfo info) {
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
				info.getLanguage(), info.getTimezone(),
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
