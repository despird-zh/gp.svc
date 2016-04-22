package com.gp.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gp.info.InfoId;
import com.gp.info.UserExInfo;
import com.gp.info.UserInfo;

public interface UserDAO extends BaseDAO<UserInfo>{

	/**
	 * check the account exist or not 
	 **/
	public boolean existAccount( String account);

	/**
	 * Query user information by account 
	 **/
	public UserInfo queryByAccount( String account);

	/**
	 * change user password 
	 **/
	public int changePassword( String account, String password);

	/**
	 * query account information. 
	 **/
	public List<UserInfo> queryAccounts( String accountname, String entity, String[] type);
	
	public int updateAsNeed(UserInfo uinfo) ;
	
	public RowMapper<UserExInfo> getUserExRowMapper();
}
