package com.gp.svc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.ServiceContext;
import com.gp.dao.impl.WorkgroupDAOImpl;
import com.gp.exception.ServiceException;
import com.gp.info.CombineInfo;
import com.gp.dao.info.GroupInfo;
import com.gp.dao.info.GroupMemberInfo;
import com.gp.dao.info.GroupUserInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.UserInfo;
import com.gp.dao.info.WorkgroupInfo;
import com.gp.dao.info.WorkgroupSumInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.svc.info.UserExtInfo;
import com.gp.svc.info.WorkgroupExt;
import com.gp.svc.info.WorkgroupLite;

public interface WorkgroupService {
	
	public static final String CTX_KEY_PUBCAPACITY = "_pub_capacity";
	public static final String CTX_KEY_PRICAPACITY = "_pri_capacity";
	public static final String CTX_KEY_IMAGE_PATH = "_image_path";
	
	/**
	 * Create the workgroup information, build public and private cabinets at the same time.
	 **/
	public boolean newWorkgroup(ServiceContext svcctx, WorkgroupInfo winfo,Long pubcapacity, Long pricapacity) throws ServiceException;
	
	/**
	 * Update the work group information 
	 **/
	public boolean updateWorkgroup(ServiceContext svcctx, WorkgroupInfo winfo,Long pubcapacity, Long pricapacity) throws ServiceException;
	
	/**
	 * get workgroup information by key 
	 **/
	public WorkgroupInfo getWorkgroup(ServiceContext svcctx, InfoId<Long> wkey) throws ServiceException;
	
	public CombineInfo<WorkgroupInfo,WorkgroupExt> getWorkgroupEx(ServiceContext svcctx, InfoId<Long> wkey) throws ServiceException;
	
	public boolean addWorkgroupMember(ServiceContext svcctx,InfoId<Long> wkey, GroupUserInfo wminfo) throws ServiceException;

	public boolean removeWorkgroupMember(ServiceContext svcctx, InfoId<Long> wkey, String account) throws ServiceException;
	
	public List<GroupMemberInfo> getWorkgroupMembers(ServiceContext svcctx, InfoId<Long> wkey, String uname , 
			InfoId<Integer> sourceId) throws ServiceException;
	
	
	public PageWrapper<GroupMemberInfo> getWorkgroupMembers(ServiceContext svcctx, InfoId<Long> wkey, String uname , 
			InfoId<Integer> sourceId, PageQuery pagequery) throws ServiceException;
	
	/**
	 * Get the users that could be added to work group 
	 **/
	public List<UserExtInfo> getAvailableUsers(ServiceContext svcctx, InfoId<Long> wkey, String uname) throws ServiceException;

	public PageWrapper<UserExtInfo> getAvailableUsers(ServiceContext svcctx, InfoId<Long> wkey, String uname, PageQuery pagequery) throws ServiceException;
	
	/**
	 * Get the groups under a work group, the search condition is group name 
	 **/
	public List<GroupInfo> getWorkgroupGroups(ServiceContext svcctx, InfoId<Long> wkey, String gname) throws ServiceException;	
	
	public boolean addWorkgroupGroup(ServiceContext svcctx, GroupInfo ginfo) throws ServiceException;	
	
	public boolean removeWorkgroupGroup(ServiceContext svcctx, InfoId<Long> wkey,String gname) throws ServiceException;	
	
	public boolean removeWorkgroupGroup(ServiceContext svcctx, InfoId<Long> groupid) throws ServiceException;	
	
	public boolean addWorkgroupGroupMember(ServiceContext svcctx, InfoId<Long> groupid, String ...accounts) throws ServiceException;	
	
	public List<UserInfo> getWorkgroupGroupMembers(ServiceContext svcctx, InfoId<Long> groupid) throws ServiceException;
	
	public boolean removeWorkgroupGroupMember(ServiceContext svcctx, InfoId<Long> groupid, String ...accounts) throws ServiceException;	
	
	public List<CombineInfo<WorkgroupInfo,WorkgroupExt>> getLocalWorkgroups(ServiceContext svcctx, String gname)throws ServiceException ;
	
	public PageWrapper<CombineInfo<WorkgroupInfo,WorkgroupLite>> getLocalWorkgroups(ServiceContext svcctx, String gname, List<String> tags, PageQuery pagequery)throws ServiceException ;
	
	public List<CombineInfo<WorkgroupInfo,WorkgroupExt>> getMirrorWorkgroups(ServiceContext svcctx, String gname)throws ServiceException ;

	public WorkgroupSumInfo getWorkgroupSummary(ServiceContext svcctx, InfoId<Long> wkey) throws ServiceException ;
	
	public static RowMapper<CombineInfo<WorkgroupInfo,WorkgroupLite>> WorkgroupLiteMapper = new RowMapper<CombineInfo<WorkgroupInfo,WorkgroupLite>>(){

		@Override
		public CombineInfo<WorkgroupInfo,WorkgroupLite> mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			CombineInfo<WorkgroupInfo,WorkgroupLite> cinfo = new CombineInfo<WorkgroupInfo,WorkgroupLite>();
			WorkgroupInfo info = WorkgroupDAOImpl.WorkgroupMapper.mapRow(rs, rowNum);
			
			WorkgroupLite lite = new WorkgroupLite();
			lite.setAdminName(rs.getString("full_name"));
			lite.setImageLink(rs.getString("image_link"));
			lite.setImageFormat(rs.getString("image_format"));
			
			cinfo.setPrimary(info);
			cinfo.setExtended(lite);
			
			return cinfo;
		}
	};

	public static RowMapper<CombineInfo<WorkgroupInfo,WorkgroupExt>> WorkgroupExMapper = new RowMapper<CombineInfo<WorkgroupInfo,WorkgroupExt>>(){

		public CombineInfo<WorkgroupInfo,WorkgroupExt> mapRow(ResultSet rs, int rowNum) throws SQLException {
		
			CombineInfo<WorkgroupInfo,WorkgroupExt> cinfo = new CombineInfo<WorkgroupInfo,WorkgroupExt>();
			
			WorkgroupInfo info = WorkgroupDAOImpl.WorkgroupMapper.mapRow(rs, rowNum);
			
			WorkgroupExt ext = new WorkgroupExt();
			ext.setEntityCode(rs.getString("entity_code"));
			ext.setNodeCode(rs.getString("node_code"));
			ext.setSourceAbbr(rs.getString("abbr"));
			ext.setSourceName(rs.getString("source_name"));
			ext.setSourceShort(rs.getString("short_name"));
			ext.setAdminName(rs.getString("full_name"));
			ext.setManagerName(rs.getString("mgr_name"));
			
			cinfo.setPrimary(info);
			cinfo.setExtended(ext);
			
			return cinfo;
		}
		
	};
}
