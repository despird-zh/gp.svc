package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.dao.info.OperLogInfo;
import com.gp.info.InfoId;

public interface OperLogDAO extends BaseDAO<OperLogInfo>{

	public List<OperLogInfo> queryByAccount(String account);
	
	public List<OperLogInfo> queryByWorkgroup(InfoId<Long> wid);
	
	public List<OperLogInfo> queryByObject(InfoId<Long> objectid);
	

	public static RowMapper<OperLogInfo> ActLogMapper = new RowMapper<OperLogInfo>(){

		@Override
		public OperLogInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			OperLogInfo actlog = new OperLogInfo();
			
			InfoId<Long> logid = IdKey.OPER_LOG.getInfoId(rs.getLong("log_id"));
			actlog.setInfoId(logid);
			actlog.setAccount(rs.getString("account"));
			actlog.setOperation(rs.getString("operation"));
			actlog.setOperationTime(rs.getTimestamp("operation_time"));
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
