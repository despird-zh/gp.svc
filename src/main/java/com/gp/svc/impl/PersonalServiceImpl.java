package com.gp.svc.impl;


import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gp.dao.impl.ImageDAOImpl;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.FlatColumns;
import com.gp.common.GroupUsers;
import com.gp.common.GroupUsers.GroupType;
import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.common.Images;
import com.gp.common.ServiceContext;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.common.DataSourceHolder;
import com.gp.dao.GroupUserDAO;
import com.gp.dao.ImageDAO;
import com.gp.dao.MemberSettingDAO;
import com.gp.dao.OrgHierDAO;
import com.gp.dao.PseudoDAO;
import com.gp.dao.UserDAO;
import com.gp.dao.UserSumDAO;
import com.gp.dao.WorkgroupDAO;
import com.gp.exception.ServiceException;
import com.gp.dao.info.ChatMessageInfo;
import com.gp.info.CombineInfo;
import com.gp.info.FlatColLocator;
import com.gp.dao.info.GroupMemberInfo;
import com.gp.dao.info.ImageInfo;
import com.gp.dao.info.MemberSettingInfo;
import com.gp.info.InfoId;
import com.gp.dao.info.OrgHierInfo;
import com.gp.dao.info.UserInfo;
import com.gp.dao.info.UserSumInfo;
import com.gp.dao.info.WorkgroupInfo;
import com.gp.pagination.PageQuery;
import com.gp.svc.CommonService;
import com.gp.svc.PersonalService;

@Service
public class PersonalServiceImpl implements PersonalService{

	static Logger LOGGER = LoggerFactory.getLogger(PersonalServiceImpl.class);
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Autowired
	OrgHierDAO orghierdao;
	
	@Autowired
	WorkgroupDAO workgroupdao;
	
	@Autowired
	UserSumDAO usersumdao;
	
	@Autowired
	UserDAO userdao;
	
	@Autowired
	ImageDAO imagedao;
	
	@Autowired
	MemberSettingDAO mbrsettingdao;

	@Autowired
	CommonService idService;
	
	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public List<CombineInfo<WorkgroupInfo, Boolean>> getWorkgroups(ServiceContext svcctx, String account) throws ServiceException {
		
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_workgroup_mbrs ");
		SQL.append("WHERE account = ? AND group_type = '").append(GroupType.WORKGROUP_MBR.name()).append("'");
		
		Object[] params = new Object[]{
			account
		};

		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}",SQL.toString(), Arrays.toString(params));
		}
		InfoId<?>[] ids = null;
		List<CombineInfo<WorkgroupInfo, Boolean>> result = new ArrayList<CombineInfo<WorkgroupInfo, Boolean>>();
		try{
			List<GroupMemberInfo> members = jtemplate.query(SQL.toString(), params, GroupUserDAO.GroupMemberMapper);
			ids = new InfoId<?>[members.size()];
			int count = 0;
			Map<InfoId<?>, Boolean> settings = new HashMap<InfoId<?>, Boolean>();
			for(GroupMemberInfo minfo : members){
				ids[count] = IdKeys.getInfoId( IdKey.GP_WORKGROUPS, minfo.getManageId());
				settings.put(ids[count], minfo.getPostVisible());
				count ++;
			}
			
			List<WorkgroupInfo> winfos =  workgroupdao.queryByIds(ids);
			for(WorkgroupInfo winfo: winfos){
				CombineInfo<WorkgroupInfo, Boolean> cinfo = new CombineInfo<WorkgroupInfo, Boolean>();
				cinfo.setPrimary(winfo);
				cinfo.setExtended(settings.get(winfo.getInfoId()));
				result.add(cinfo);
			}
			
			return result;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "workgroup", Arrays.toString(ids));
		}
	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public List<CombineInfo<OrgHierInfo, Boolean>> getOrgHierNodes(ServiceContext svcctx, String account) throws ServiceException {
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_orghier_mbrs ");
		SQL.append("WHERE account = ? AND group_type = '").append(GroupType.ORG_HIER_MBR.name()).append("'");
		
		Object[] params = new Object[]{
				account
			};
		
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}",SQL.toString(), Arrays.toString(params));
		}
		List<CombineInfo<OrgHierInfo, Boolean>> result = new ArrayList<CombineInfo<OrgHierInfo, Boolean>>();
		InfoId<?>[] ids = null;
		try{
			List<GroupMemberInfo> members = jtemplate.query(SQL.toString(), params, GroupUserDAO.GroupMemberMapper);
			ids = new InfoId<?>[members.size()];
			int count = 0;
			Map<InfoId<?>, Boolean> settings = new HashMap<InfoId<?>, Boolean>();
			for(GroupMemberInfo minfo : members){
				ids[count] = IdKeys.getInfoId(IdKey.GP_ORG_HIER, minfo.getManageId());
				settings.put(ids[count], minfo.getPostVisible());
				count ++;
			}
			
			List<OrgHierInfo> oinfos = orghierdao.queryByIds(ids);
			for(OrgHierInfo oinfo: oinfos){
				CombineInfo<OrgHierInfo, Boolean> cinfo = new CombineInfo<OrgHierInfo, Boolean>();
				cinfo.setPrimary(oinfo);
				cinfo.setExtended(settings.get(oinfo.getInfoId()));
				result.add(cinfo);
			}
			return result;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "orghier", Arrays.toString(ids));
		}
	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public List<GroupMemberInfo> getWorkgroupMembers(ServiceContext svcctx, String account)
			throws ServiceException {
		
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_workgroup_mbrs ");
		SQL.append("WHERE account = :account AND group_type = '").append(GroupType.WORKGROUP_MBR.name()).append("'");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}",SQL.toString(), params.toString());
		}

		try{
			return jtemplate.query(SQL.toString(), params, GroupUserDAO.GroupMemberMapper);
		
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "group member", params.toString());
		}
	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public List<GroupMemberInfo> getOrgHierMembers(ServiceContext svcctx, String account)
			throws ServiceException {
		
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_orghier_mbrs ");
		SQL.append("WHERE account = :account AND group_type = '").append(GroupType.ORG_HIER_MBR.name()).append("'");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("SQL : {} / PARAMS : {}",SQL.toString(), params.toString());
		}

		try{
			return jtemplate.query(SQL.toString(), params, GroupUserDAO.GroupMemberMapper);
		
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.query.with", dae, "orghier member", params.toString());
		}
	}
	
	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public UserSumInfo getUserSummary(ServiceContext svcctx, String account) throws ServiceException {
		
		try{
			
			return usersumdao.queryByAccount(account);
			
		}catch(DataAccessException dae){
			throw new ServiceException("excp.query.with", dae, "user sumamry", account);
		}
	}

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public List<ChatMessageInfo> getChatMessages(ServiceContext svcctx, String account, PageQuery pquery)
			throws ServiceException {
		
		return null;
	}

	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public boolean updateBasicSetting(ServiceContext svcctx, UserInfo uinfo, String avatarImg) throws ServiceException {
		
		FlatColLocator[] cols = new FlatColLocator[]{
			FlatColumns.AVATAR_ID,
			FlatColumns.PHONE,
			FlatColumns.TYPE,
			FlatColumns.EMAIL,
			FlatColumns.MOBILE,
			FlatColumns.FULL_NAME,
			FlatColumns.STATE,
			FlatColumns.SIGNATURE,
		};
		
		svcctx.setTraceInfo(uinfo);
		
		try{
			// create image firstly.
			String filename = FilenameUtils.getName(avatarImg);
			Long imgid = Images.parseImageId(filename);
			ImageInfo imginfo = imagedao.query(IdKeys.getInfoId(IdKey.GP_IMAGES, imgid));
			// check if the image exists
			if(imginfo == null){ // save the image

				imginfo = ImageDAOImpl.parseLocalImageInfo(avatarImg);
				imginfo.setCategory(Images.Category.USER_AVATAR.name());
				svcctx.setTraceInfo(imginfo);
				imagedao.create(imginfo);
			}
			uinfo.setAvatarId(imgid);
			int cnt = userdao.update(uinfo, FilterMode.INCLUDE, cols);
			
			return cnt > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.update", dae, "user setting");
		}
		
	}

	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public boolean updateBelongSetting(ServiceContext svcctx, String account,Map<InfoId<Long>, Boolean> settings) throws ServiceException {
		
		try{
			int cnt = 0;
			for(Map.Entry<InfoId<Long>, Boolean> entry : settings.entrySet()){
				// check the record existence
				MemberSettingInfo mbrinfo = mbrsettingdao.queryByMember(entry.getKey(), account);
				if(null == mbrinfo){
					
					mbrinfo = new MemberSettingInfo();
					InfoId<Long> relid = idService.generateId(IdKey.GP_MBR_SETTING, Long.class);
					mbrinfo.setInfoId(relid);
					mbrinfo.setManageId(entry.getKey().getId());
					String type = null;
					if(IdKey.GP_ORG_HIER.getSchema().equals(entry.getKey().getIdKey()))
						type = GroupUsers.GroupType.ORG_HIER_MBR.name();
					else if(IdKey.GP_WORKGROUPS.getSchema().equals(entry.getKey().getIdKey()))
						type = GroupUsers.GroupType.WORKGROUP_MBR.name();
					
					mbrinfo.setAccount(account);
					mbrinfo.setGroupType(type);
					mbrinfo.setPostVisible(entry.getValue());
					svcctx.setTraceInfo(mbrinfo);
					
					cnt += mbrsettingdao.create(mbrinfo);
					
				}else{
					
					cnt += pseudodao.update(mbrinfo.getInfoId(), FlatColumns.POST_VISIBLE, entry.getValue());
				}
			}
			return cnt > 0;
		}catch(DataAccessException dae){
			throw new ServiceException("excp.update",dae,"member setting");
		}
	}

	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public boolean updateStorageSetting(ServiceContext svcctx,InfoId<Long> userid, Long storageId, Long publishcap, Long netdiskcap)
			throws ServiceException {
		
		try{
			int cnt = 0;
			// update the storage id
			cnt = pseudodao.update(userid, FlatColumns.STORAGE_ID, storageId);
			UserInfo uinfo = userdao.query(userid);
			InfoId<Long> pubcabid = IdKeys.getInfoId(IdKey.GP_CABINETS, uinfo.getPublishCabinet());
			Map<FlatColLocator, Object> fields = new HashMap<FlatColLocator, Object>();
			fields.put(FlatColumns.STORAGE_ID, storageId);
			fields.put(FlatColumns.CAPACITY, publishcap);
			cnt += pseudodao.update(pubcabid, fields);
			InfoId<Long> pricabid = IdKeys.getInfoId(IdKey.GP_CABINETS, uinfo.getNetdiskCabinet());
			// replace the capacity with netdisk's setting
			fields.put(FlatColumns.CAPACITY, netdiskcap);
			cnt += pseudodao.update(pricabid, fields);
		
			return cnt == 3; 
		}catch(DataAccessException dae){
			throw new ServiceException("excp.update",dae,"user's storage setting");
		}
		
	}

	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public boolean updateRegionSetting(ServiceContext svcctx, InfoId<Long> userid, String timezone, String language)
			throws ServiceException {
		
		try{
			int cnt = 0;
			
			Map<FlatColLocator, Object> fields = new HashMap<FlatColLocator, Object>();
			fields.put(FlatColumns.TIMEZONE, timezone);
			fields.put(FlatColumns.LANGUAGE, language);
			
			cnt = pseudodao.update(userid, fields);
		
			return cnt >0 ; 
		}catch(DataAccessException dae){
			throw new ServiceException("excp.update",dae,"user's region setting");
		}
	}

}
