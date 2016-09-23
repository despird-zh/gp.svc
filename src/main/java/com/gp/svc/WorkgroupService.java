package com.gp.svc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
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
import com.gp.svc.info.WorkgroupExtInfo;
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
	
	public WorkgroupExtInfo getWorkgroupExt(ServiceContext svcctx, InfoId<Long> wkey) throws ServiceException;
	
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
	
	public List<WorkgroupExtInfo> getLocalWorkgroups(ServiceContext svcctx, String gname)throws ServiceException ;
	
	public PageWrapper<CombineInfo<WorkgroupInfo,WorkgroupLite>> getLocalWorkgroups(ServiceContext svcctx, String gname, List<String> tags, PageQuery pagequery)throws ServiceException ;
	
	public List<WorkgroupExtInfo> getMirrorWorkgroups(ServiceContext svcctx, String gname)throws ServiceException ;

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

	public static RowMapper<WorkgroupExtInfo> WorkgroupExMapper = new RowMapper<WorkgroupExtInfo>(){

		public WorkgroupExtInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

			WorkgroupExtInfo info = new WorkgroupExtInfo();

			InfoId<Long> id = IdKey.WORKGROUP.getInfoId(rs.getLong("workgroup_id"));
			info.setInfoId(id);
			
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupName(rs.getString("workgroup_name"));
			info.setDescription(rs.getString("descr"));
			info.setState(rs.getString("state"));
			info.setAdmin(rs.getString("admin"));
			info.setManager(rs.getString("manager"));
			info.setCreator(rs.getString("creator"));
			info.setCreateDate(rs.getTimestamp("create_time"));
			info.setStorageId(rs.getInt("storage_id"));
			info.setPublishCabinet(rs.getLong("publish_cab_id"));
			info.setNetdiskCabinet(rs.getLong("netdisk_cab_id"));
			info.setOrgId(rs.getLong("org_id"));
			info.setHashCode(rs.getString("hash_code"));
			info.setOwm(rs.getLong("owm"));
			info.setShareEnable(rs.getBoolean("share_enable"));
			info.setLinkEnable(rs.getBoolean("link_enable"));
			info.setPostEnable(rs.getBoolean("post_enable"));
			info.setNetdiskEnable(rs.getBoolean("netdisk_enable"));
			info.setPublishEnable(rs.getBoolean("publish_enable"));
			info.setTaskEnable(rs.getBoolean("task_enable"));
			info.setAvatarId(rs.getLong("avatar_id"));
			info.setMemberGroupId(rs.getLong("mbr_group_id"));
			info.setParentId(rs.getLong("workgroup_pid"));
			info.setPostAcceptable(rs.getBoolean("mbr_post_acpt"));
			info.setPublicFlowId(rs.getLong("public_flow_id"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			info.setEntityCode(rs.getString("entity_code"));
			info.setNodeCode(rs.getString("node_code"));
			info.setSourceAbbr(rs.getString("abbr"));
			info.setSourceName(rs.getString("source_name"));
			info.setSourceShort(rs.getString("short_name"));
			info.setAdminName(rs.getString("full_name"));
			info.setManagerName(rs.getString("mgr_name"));
		
			return info;
		}
		
	};
}
