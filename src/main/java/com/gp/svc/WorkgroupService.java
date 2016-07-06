package com.gp.svc;

import java.util.List;
import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.CombineInfo;
import com.gp.info.GroupInfo;
import com.gp.info.GroupMemberInfo;
import com.gp.info.GroupUserInfo;
import com.gp.info.InfoId;
import com.gp.info.UserInfo;
import com.gp.info.WorkgroupInfo;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.svc.info.UserExt;
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
	public List<CombineInfo<UserInfo, UserExt>> getAvailableUsers(ServiceContext svcctx, InfoId<Long> wkey, String uname) throws ServiceException;

	public PageWrapper<CombineInfo<UserInfo, UserExt>> getAvailableUsers(ServiceContext svcctx, InfoId<Long> wkey, String uname, PageQuery pagequery) throws ServiceException;
	
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

}
