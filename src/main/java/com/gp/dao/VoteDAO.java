package com.gp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.info.InfoId;
import com.gp.dao.info.VoteInfo;

public interface VoteDAO extends BaseDAO<VoteInfo>{

	/**
	 * Query the vote information of account on resource 
	 **/
	public VoteInfo queryByAccount(InfoId<Long> resourceId, String account);

	/**
	 * Delete the vote information by resource id
	 * @param resourceId the id of resource
	 **/
	public int deleteByResource(InfoId<Long> resourceId);

	/**
	 * Query the like vote count
	 * @param resourceId the id of resource
	 **/
	public int queryVoteCount(InfoId<Long> resourceId, String opinion);

	public static RowMapper<VoteInfo> VoteMapper = new RowMapper<VoteInfo>(){

		@Override
		public VoteInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			VoteInfo info = new VoteInfo();
			InfoId<Long> id = IdKeys.getInfoId(IdKey.GP_VOTES,rs.getLong("vote_id"));
			info.setInfoId(id);
			
			info.setWorkgroupId(rs.getLong("workgroup_id"));
			info.setResourceId(rs.getLong("resource_id"));
			info.setResourceType(rs.getString("resource_type"));
			info.setVoter(rs.getString("voter"));
			info.setOpinion(rs.getString("opinion"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
}
