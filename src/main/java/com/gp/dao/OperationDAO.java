package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.dao.info.OperationInfo;
import com.gp.info.InfoId;

public interface OperationDAO extends BaseDAO<OperationInfo>{

	public List<OperationInfo> queryByAccount(String account);
	
	public List<OperationInfo> queryByWorkgroup(InfoId<Long> wid);
	
	public List<OperationInfo> queryByObject(InfoId<Long> objectid);
	

	public static RowMapper<OperationInfo> ActLogMapper = new RowMapper<OperationInfo>(){

		@Override
		public OperationInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			OperationInfo actlog = new OperationInfo();
			
			InfoId<Long> logid = IdKeys.getInfoId(IdKey.GP_OPERATIONS, rs.getLong("oper_id"));
			actlog.setInfoId(logid);
			actlog.setSubject(rs.getString("subject"));
			actlog.setOperation(rs.getString("operation"));
			actlog.setOperationTime(rs.getTimestamp("operation_time"));
			actlog.setOperationExcerpt(rs.getString("operation_excerpt"));
			actlog.setSubjectExcerpt(rs.getString("subject_excerpt"));
			actlog.setAuditId(rs.getLong("audit_id"));
			
			actlog.setObject(rs.getString("object"));
			actlog.setObjectExcerpt(rs.getString("object_excerpt"));
			actlog.setPredicate(rs.getString("predicate"));
			actlog.setPredicateExcerpt(rs.getString("predicate_excerpt"));
			actlog.setWorkgroupId(rs.getLong("workgroup_id"));
			
			actlog.setModifier(rs.getString("modifier"));
			actlog.setModifyDate(rs.getTimestamp("last_modified"));
			return actlog;
		}};
}
