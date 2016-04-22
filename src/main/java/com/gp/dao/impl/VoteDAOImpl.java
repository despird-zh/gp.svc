package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.dao.VoteDAO;
import com.gp.info.InfoId;
import com.gp.info.VoteInfo;

@Component("voteDAO")
public class VoteDAOImpl extends DAOSupport implements VoteDAO{

	static Logger LOGGER = LoggerFactory.getLogger(VoteDAOImpl.class);
	
	@Autowired
	public VoteDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( VoteInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_votes (")
			.append("workgroup_id,resource_id,")
			.append("resource_type,vote_id,voter,opinion,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,")
			.append("?,?,?,?,")
			.append("?,?)");
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				info.getWorkgroupId(),info.getResourceId(),
				info.getResourceType(),key.getId(),info.getVoter(),info.getOpinion(),
				info.getModifier(),info.getModifyDate()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		return jtemplate.update(SQL.toString(),params);
	}

	@Override
	public int delete( InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_votes ")
			.append("where vote_id = ?");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public int update( VoteInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_votes set ")
			.append("workgroup_id = ?,resource_id = ?,")
			.append("resource_type = ?,voter = ?,opinion = ?,")
			.append("modifier = ?, last_modified = ? ")
			.append("where vote_id = ?");
		
		Object[] params = new Object[]{
				info.getWorkgroupId(),info.getResourceId(),
				info.getResourceType(),info.getVoter(),info.getOpinion(),
				info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params);
		return rtv;
	}

	@Override
	public VoteInfo query( InfoId<?> id) {
		String SQL = "select * from gp_votes "
				+ "where vote_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		VoteInfo ainfo = jtemplate.queryForObject(SQL, params, VoteMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<VoteInfo> VoteMapper = new RowMapper<VoteInfo>(){

		@Override
		public VoteInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			VoteInfo info = new VoteInfo();
			InfoId<Long> id = IdKey.VOTE.getInfoId(rs.getLong("vote_id"));
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

	@Override
	public RowMapper<VoteInfo> getRowMapper() {
		
		return VoteMapper;
	}
}
