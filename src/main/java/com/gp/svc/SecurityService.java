package com.gp.svc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.gp.common.ServiceContext;
import com.gp.common.GroupUsers;
import com.gp.common.IdKey;
import com.gp.exception.ServiceException;
import com.gp.info.CombineInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.KVPair;
import com.gp.dao.impl.DAOSupport;
import com.gp.dao.impl.UserDAOImpl;
import com.gp.dao.info.TokenInfo;
import com.gp.dao.info.UserInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.svc.info.UserExtInfo;
import com.gp.svc.info.UserLiteInfo;
import org.springframework.jdbc.core.RowMapper;

public interface SecurityService {
	
	public boolean existAccount(ServiceContext svcctx, String account) throws ServiceException;
	
	/**
	 * create new account, system will create public and private cabinets for user
	 * the capacity of cabinets will be pushed in service context, then be extract 
	 * during creating new account. 
	 **/
	public boolean newAccount(ServiceContext svcctx, UserInfo uinfo, Long pubcapacity, Long pricapacity) throws ServiceException;
	
	/**
	 * Update the user information
	 * @param uinfo the user information 
	 **/
	public int updateAccount(ServiceContext svcctx, UserInfo uinfo, Long pubcapacity, Long pricapacity) throws ServiceException;
	
	/**
	 * Create a new external account
	 * @param uinfo the user information 
	 **/
	public boolean newAccountExt(ServiceContext svcctx, UserInfo uinfo) throws ServiceException;
	
	/**
	 * Get the lite user account information
	 * 
	 * @param userId the userId, if userId is null, then utilize the account parameter
	 * @param account the account, i.e login 
	 * @param type the type of user: inner / ldap / external 
	 * 
	 **/
	public UserInfo getAccountLite(ServiceContext svcctx,InfoId<Long> userId, String account, String type) throws ServiceException;
		
	/**
	 * Get the full user account information
	 * 
	 * @param userId the userId, if userId is null, then utilize the account parameter
	 * @param account the account, i.e login 
	 * @param type the type of user: inner / ldap / external 
	 **/
	public UserExtInfo getAccountFull(ServiceContext svcctx,InfoId<Long> userId, String account, String type) throws ServiceException;
		
	/**
	 * Query the account list 
	 * 
	 * @param accountname the account or name as query condition
	 * @param instanceId the id of instance
	 * @param type the types condition
	 * @param state the states condition
	 **/
	public List<UserExtInfo> getAccounts(ServiceContext svcctx, String accountname, Integer sourId, String[] type,String[] state) throws ServiceException;

	/**
	 * Query the account list
	 *
	 * @param accounts the account list
	 * @param userids the user id list
	 **/
	public List<UserLiteInfo> getAccounts(ServiceContext svcctx, List<Long> userids, List<String> accounts) throws ServiceException;


	/**
	 * Query the account list per page support pagination request
	 * 
	 * @param accountname the account or name as query condition
	 * @param sourceId the id of source entity
	 * @param type the types condition
	 * @param pagequery the page query condition
	 *  
	 **/
	public PageWrapper<UserExtInfo> getAccounts(ServiceContext svcctx, String accountname, Integer sourceId, String[] type, PageQuery pagequery) throws ServiceException;
	
	/**
	 * Query Roles of account in Greoupress System
	 * 
	 * @param wgroupId the work group id
	 * @param account the account i.e login
	 **/
	public String getAccountRole(ServiceContext svcctx, InfoId<Long> wgroupId, String account) throws ServiceException;

	/**
	 * Query Groups of account in a work group
	 * 
	 * @param wgroupId the work group id
	 * @param account the account i.e login
	 *
	 * @return KVPair's key is the id of group ; value is the name of group
	 **/
	public Set<KVPair<Long, String>> getAccountGroups(ServiceContext svcctx, InfoId<Long> wgroupId, String account) throws ServiceException;
	
	/**
	 * Remove the account information
	 * @param userId the id of user
	 * @param account the account i.e login 
	 **/
	public boolean removeAccount(ServiceContext svcctx, InfoId<Long> userId, String account) throws ServiceException;

	/**
	 * Change the account password
	 * @param account the account i.e login 
	 * @param password the password of account
	 **/
	public boolean changePassword(ServiceContext svcctx, String account, String password) throws ServiceException;

	/**
	 * Change the account state
	 * @param userId the id of user
	 * @param state the state of account
	 **/
	public boolean changeAccountState(ServiceContext svcctx, InfoId<Long> userId, GroupUsers.UserState state) throws ServiceException;
	
	/**
	 * Update the Logon trace information
	 * @param userId the id of user
	 * @param resetRetry flag to resetRetry :true - reset to normal ; false count retry times
	 **/
	public boolean updateLogonTrace(ServiceContext svcctx, InfoId<Long> userId,boolean resetRetry)throws ServiceException;

	
	/**
	 * get a token from database 
	 **/
	public TokenInfo getToken(ServiceContext svcctx, InfoId<Long> tokenKey) throws ServiceException;
	
	/**
	 * new a token in database 
	 **/
	public boolean newToken(ServiceContext svcctx, TokenInfo token) throws ServiceException;
	
	/**
	 * refresh the token in database. 
	 **/
	public boolean refreshToken(ServiceContext svcctx, TokenInfo token) throws ServiceException;
	
	/**
	 * remove the token from database 
	 **/
	public boolean removeToken(ServiceContext svcctx, InfoId<Long> tokenId) throws ServiceException;

	public static RowMapper<UserLiteInfo> USER_LITE_ROW_MAPPER = new RowMapper<UserLiteInfo>() {
		@Override
		public UserLiteInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserLiteInfo info = new UserLiteInfo();
			info.setAccount(rs.getString("account"));
			info.setUserId(rs.getLong("user_id"));
			info.setAvatarLink(rs.getString("image_link"));
			info.setEmail(rs.getString("email"));
			info.setSourceName(rs.getString("source_name"));
			info.setSourceId(rs.getLong("source_id"));
			info.setUserName(rs.getString("full_name"));
			info.setSourceAbbr(rs.getString("abbr"));
			
			return info;
		}
	};

	public static RowMapper<UserExtInfo> UserExMapper = new RowMapper<UserExtInfo>(){

		@Override
		public UserExtInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

			// save extend data
			UserExtInfo info = new UserExtInfo();
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
			info.setTimezone(rs.getString("timezone"));
			info.setPublishCabinet(rs.getLong("publish_cabinet_id"));
			info.setNetdiskCabinet(rs.getLong("netdisk_cabinet_id"));
			info.setGlobalAccount(rs.getString("global_account"));
			info.setStorageId(rs.getInt("storage_id"));
			info.setAvatarId(rs.getLong("avatar_id"));
			info.setClassification(rs.getString("classification"));
			info.setSignature(rs.getString("signature"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			if(DAOSupport.hasColInResultSet(rs, "storage_name")){
				info.setStorageName(rs.getString("storage_name"));
			}
			info.setSourceAbbr(rs.getString("abbr"));
			info.setSourceShortName(rs.getString("short_name"));
			info.setSourceName(rs.getString("source_name"));

			return info;
		}};
}
