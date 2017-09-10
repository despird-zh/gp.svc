package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.info.InfoId;
import com.gp.dao.info.WorkgroupInfo;

public interface WorkgroupDAO extends BaseDAO<WorkgroupInfo>{

	List<WorkgroupInfo> queryByIds(InfoId<?> ... ids);
	
	public int delete( InfoId<?> id, boolean logic);

	public static RowMapper<WorkgroupInfo> WorkgroupMapper = new RowMapper<WorkgroupInfo>(){

		public WorkgroupInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		
			WorkgroupInfo info = new WorkgroupInfo();
			
			InfoId<Long> id = IdKeys.getInfoId(IdKey.GP_WORKGROUPS, rs.getLong("workgroup_id"));
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
			info.setTraceCode(rs.getString("hash_code"));
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
			info.setDelFlag(rs.getBoolean("del_flag"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
		
	};
}
