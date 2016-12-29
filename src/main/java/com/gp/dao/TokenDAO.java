package com.gp.dao;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.dao.info.TokenInfo;
import com.gp.info.InfoId;

public interface TokenDAO extends BaseDAO<TokenInfo>{

	public static RowMapper<TokenInfo> TokenMapper = new RowMapper<TokenInfo>(){

		@Override
		public TokenInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			TokenInfo info = new TokenInfo();
			InfoId<Long> id = IdKey.TOKEN.getInfoId(rs.getLong("token_id"));
			info.setInfoId(id);
			info.setAudience(rs.getString("audience"));
			info.setClaims(rs.getString("claims"));
			info.setIssuer(rs.getString("issuer"));
			info.setSubject(rs.getString("subject"));
			info.setExpireTime(rs.getTimestamp("expire_time"));
			info.setIssueAt(rs.getTimestamp("issue_at"));
			info.setNotBefore(rs.getTimestamp("not_before"));
			info.setJwtToken(rs.getString("jwt_token"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
		
	};
}
