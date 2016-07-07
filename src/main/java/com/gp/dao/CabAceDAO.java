package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.CabAceInfo;
import com.gp.info.InfoId;

public interface CabAceDAO extends BaseDAO<CabAceInfo>{

	public CabAceInfo queryBySubject(Long aclid,String type, String subject);
	
	public int deleteByAcl(Long aclid);
	
	public int deleteBySubject(Long aclid, String type,String subject);
	
	public List<CabAceInfo> queryByAcl(Long aclid);
	
	public static RowMapper<CabAceInfo> CabAceMapper = new RowMapper<CabAceInfo>(){

		@Override
		public CabAceInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			CabAceInfo info = new CabAceInfo();
			InfoId<Long> id = IdKey.CAB_ACE.getInfoId(rs.getLong("ace_id"));
			
			info.setInfoId(id);
			info.setAclId(rs.getLong("acl_id"));
			info.setSubject(rs.getString("subject"));
			info.setSubjectType(rs.getString("subject_type"));
			info.setPrivilege(rs.getInt("privilege"));
			info.setPermissions(rs.getString("perm_json"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}};
}
