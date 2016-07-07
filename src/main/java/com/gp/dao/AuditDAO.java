package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.info.AuditInfo;
import com.gp.info.InfoId;

public interface AuditDAO extends BaseDAO<AuditInfo>{


	public static RowMapper<AuditInfo> AuditMapper = new RowMapper<AuditInfo>(){

		@Override
		public AuditInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		
			AuditInfo info = new AuditInfo();

			InfoId<Long> id = IdKey.AUDIT.getInfoId(rs.getLong("audit_id"));
			info.setInfoId(id);
			
			info.setWorkgroupId(rs.getLong("workgroup_id"));

			info.setClient(rs.getString("client"));
			info.setHost(rs.getString("host"));
			info.setApp(rs.getString("app"));
			info.setVersion(rs.getString("version"));
			
			info.setVerb(rs.getString("verb"));
			info.setSubject(rs.getString("subject"));
			info.setPredicates(rs.getString("predicate_json"));
			info.setTarget(rs.getString("object"));
			info.setState(rs.getString("state"));
			info.setMessage(rs.getString("message"));
			info.setAuditDate(rs.getTimestamp("audit_time"));
			info.setElapseTime(rs.getLong("elapse_time"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
		
	};
}
