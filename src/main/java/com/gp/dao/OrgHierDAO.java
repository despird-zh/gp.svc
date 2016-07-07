package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.InfoId;
import com.gp.info.OrgHierInfo;

public interface OrgHierDAO extends BaseDAO<OrgHierInfo>{

	List<OrgHierInfo> queryByIds(InfoId<?> ... ids);
	
	public static RowMapper<OrgHierInfo> OrgHierMapper = new RowMapper<OrgHierInfo>(){

		@Override
		public OrgHierInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			OrgHierInfo info = new OrgHierInfo();
			InfoId<Long> id = IdKey.ORG_HIER.getInfoId(rs.getLong("org_id"));
			info.setInfoId(id);
			
			info.setMemberGroupId(rs.getLong("mbr_group_id"));
			info.setLevel(rs.getString("org_level"));
			info.setParentOrg(rs.getLong("org_pid"));
			info.setOrgName(rs.getString("org_name"));
			info.setAdmin(rs.getString("admin"));
			info.setEmail(rs.getString("email"));
			info.setManager(rs.getString("manager"));
			info.setDescription(rs.getString("descr"));
			info.setPostAcceptable(rs.getBoolean("mbr_post_acpt"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
	};
}
