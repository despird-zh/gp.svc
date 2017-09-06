package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.info.InfoId;
import com.gp.dao.info.ShareToInfo;

public interface ShareToDAO extends BaseDAO<ShareToInfo>{


	public static RowMapper<ShareToInfo> ShareToMapper = new RowMapper<ShareToInfo>(){

		@Override
		public ShareToInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			ShareToInfo info = new ShareToInfo();
			
			InfoId<Long> id = IdKeys.getInfoId(IdKey.GP_SHARE_TO,rs.getLong("share_to_id"));
			
			info.setInfoId(id);
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setShareId(rs.getLong("share_id"));
			info.setShareName(rs.getString("share_name"));
			info.setToAccount(rs.getString("to_account"));
			info.setToGlobalAccount(rs.getString("to_global_account"));
			info.setToEmail(rs.getString("to_email"));
			info.setShareMode(rs.getString("share_mode"));
			info.setOwm(rs.getLong("owm"));
			info.setShareToken(rs.getString("share_token"));
			info.setAccessCount(rs.getInt("access_count"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
