package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.ActLogInfo;
import com.gp.info.InfoId;

public interface ActLogDAO extends BaseDAO<ActLogInfo>{

	public List<ActLogInfo> queryByAccount(String account);
	
	public List<ActLogInfo> queryByWorkgroup(InfoId<Long> wid);
	
	public List<ActLogInfo> queryByObject(InfoId<Long> objectid);
	

	public static RowMapper<ActLogInfo> ActLogMapper = new RowMapper<ActLogInfo>(){

		@Override
		public ActLogInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			ActLogInfo actlog = new ActLogInfo();
			
			InfoId<Long> logid = IdKey.ACT_LOG.getInfoId(rs.getLong("log_id"));
			actlog.setInfoId(logid);
			actlog.setAccount(rs.getString("account"));
			actlog.setActivity(rs.getString("activity"));
			actlog.setActivityDate(rs.getTimestamp("activity_date"));
			actlog.setUserName(rs.getString("user_name"));
			actlog.setAuditId(rs.getLong("audit_id"));
			actlog.setObjectId(rs.getString("object_id"));
			actlog.setObjectExcerpt(rs.getString("object_excerpt"));
			actlog.setPredicateId(rs.getString("predicate_id"));
			actlog.setPredicateExcerpt(rs.getString("predicate_excerpt"));
			actlog.setWorkgroupId(rs.getLong("workgroup_id"));
			
			actlog.setModifier(rs.getString("modifier"));
			actlog.setModifyDate(rs.getTimestamp("last_modified"));
			return actlog;
		}};
}
