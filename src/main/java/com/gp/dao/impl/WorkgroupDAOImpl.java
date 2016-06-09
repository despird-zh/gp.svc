package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.WorkgroupDAO;
import com.gp.info.InfoId;
import com.gp.info.WorkgroupExInfo;
import com.gp.info.WorkgroupInfo;

@Component("workgroupDAO")
public class WorkgroupDAOImpl extends DAOSupport implements WorkgroupDAO{

	Logger LOGGER = LoggerFactory.getLogger(WorkgroupDAOImpl.class);
	
	@Autowired
	public WorkgroupDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( WorkgroupInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_workgroups (")
			.append("source_id,workgroup_id,workgroup_name,hash_code,storage_id,")
			.append("descr,state,admin,creator,org_id,mbr_group_id,")
			.append("publish_cab_id, netdisk_cab_id,owm,publish_enable,task_enable,")
			.append("share_enable, link_enable,post_enable,netdisk_enable,avatar_id,")
			.append("create_time,modifier, last_modified")
			.append(")values(")
			.append("?,?,?,?,?,")
			.append("?,?,?,?,?,?,")
			.append("?,?,?,?,?,")
			.append("?,?,?,?,?,")
			.append("?,?,?)");

		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				info.getSourceId(),key.getId(),info.getWorkgroupName(),info.getHashCode(),info.getStorageId(),
				info.getDescription(),info.getState(),info.getAdmin(),info.getCreator(),info.getOrgId(),info.getMemberGroupId(),
				info.getPublishCabinet(),info.getNetdiskCabinet(),info.getOwm(),info.getPublishEnable(),info.getTaskEnable(),
				info.getShareEnable(),info.getLinkEnable(),info.getPostEnable(),info.getNetdiskEnable(),info.getAvatarId(),
				info.getCreateDate(),info.getModifier(),info.getModifyDate()
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
		SQL.append("delete from gp_workgroups ")
			.append("where workgroup_id = ?");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = -1;

			rtv = jtemplate.update(SQL.toString(), params);

		return rtv;
	}

	@Override
	public int update( WorkgroupInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("UPDATE gp_workgroups SET ")
			.append("workgroup_name = ?,org_id = ?,storage_id = ?,source_id = ?, mbr_group_id = ?,")
			.append("descr = ?,state = ? ,admin = ?,creator = ?,hash_code = ?,")
			.append("create_time = ?,modifier = ?, last_modified = ?,avatar_id = ?,")
			.append("publish_cab_id = ?,netdisk_cab_id = ? ,owm = ? ,publish_enable = ? ,task_enable = ? ,")
			.append("share_enable = ? , link_enable = ? ,post_enable = ? ,netdisk_enable = ? ")
			.append("WHERE workgroup_id = ?  ");
		
		Object[] params = new Object[]{
				info.getWorkgroupName(),info.getOrgId(),info.getStorageId(),info.getSourceId(),info.getMemberGroupId(),
				info.getDescription(),info.getState(),info.getAdmin(),info.getCreator(),info.getHashCode(),
				info.getCreateDate(),info.getModifier(),info.getModifyDate(),info.getAvatarId(),
				info.getPublishCabinet(),info.getNetdiskCabinet(),info.getOwm(),info.getPublishEnable(),info.getTaskEnable(),
				info.getShareEnable(),info.getLinkEnable(),info.getPostEnable(),info.getNetdiskEnable(),
				info.getInfoId().getId()
		};

		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int rtv = -1;
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		rtv = jtemplate.update(SQL.toString(), params);

		return rtv;
	}

	@Override
	public WorkgroupInfo query( InfoId<?> id) {
		
		String SQL = "SELECT * FROM gp_workgroups WHERE workgroup_id = ?";
		
		Object[] params = new Object[]{				
				id.getId()
			};

		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + ArrayUtils.toString(params));
		}
		List<WorkgroupInfo> infos = jtemplate.query(SQL, params, WorkgroupMapper);

		return CollectionUtils.isEmpty(infos) ? null : infos.get(0);
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static RowMapper<WorkgroupInfo> WorkgroupMapper = new RowMapper<WorkgroupInfo>(){

		public WorkgroupInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		
			WorkgroupInfo info = new WorkgroupInfo();
			
			InfoId<Long> id = IdKey.WORKGROUP.getInfoId(rs.getLong("workgroup_id"));
			info.setInfoId(id);
			
			info.setSourceId(rs.getInt("source_id"));
			info.setWorkgroupName(rs.getString("workgroup_name"));
			info.setDescription(rs.getString("descr"));
			info.setState(rs.getString("state"));
			info.setAdmin(rs.getString("admin"));
			info.setCreator(rs.getString("creator"));
			info.setCreateDate(rs.getTimestamp("create_time"));
			info.setStorageId(rs.getInt("storage_id"));
			info.setPublishCabinet(rs.getLong("publish_cab_id"));
			info.setNetdiskCabinet(rs.getLong("netdisk_cab_id"));
			info.setOrgId(rs.getLong("org_id"));
			info.setHashCode(rs.getString("hash_code"));
			info.setOwm(rs.getLong("owm"));
			info.setShareEnable(rs.getBoolean("share_enable"));
			info.setLinkEnable(rs.getBoolean("link_enable"));
			info.setPostEnable(rs.getBoolean("post_enable"));
			info.setNetdiskEnable(rs.getBoolean("netdisk_enable"));
			info.setPublishEnable(rs.getBoolean("publish_enable"));
			info.setTaskEnable(rs.getBoolean("task_enable"));
			info.setAvatarId(rs.getLong("avatar_id"));
			info.setMemberGroupId(rs.getLong("mbr_group_id"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
		
	};

	@Override
	public RowMapper<WorkgroupInfo> getRowMapper() {

		return WorkgroupMapper;
	}

	public static RowMapper<WorkgroupExInfo> WorkgroupExMapper = new RowMapper<WorkgroupExInfo>(){

		public WorkgroupExInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		
			WorkgroupExInfo info = new WorkgroupExInfo();
			
			InfoId<Long> id = IdKey.WORKGROUP.getInfoId(rs.getLong("workgroup_id"));
			info.setInfoId(id);			
			info.setSourceId(rs.getInt("source_id"));			
			info.setWorkgroupName(rs.getString("workgroup_name"));
			info.setDescription(rs.getString("descr"));
			info.setState(rs.getString("state"));
			info.setAdmin(rs.getString("admin"));
			info.setCreator(rs.getString("creator"));
			info.setCreateDate(rs.getTimestamp("create_time"));
			info.setStorageId(rs.getInt("storage_id"));
			info.setPublishCabinet(rs.getLong("publish_cab_id"));
			info.setNetdiskCabinet(rs.getLong("netdisk_cab_id"));
			info.setOrgId(rs.getLong("org_id"));
			info.setHashCode(rs.getString("hash_code"));
			info.setOwm(rs.getLong("owm"));
			info.setShareEnable(rs.getBoolean("share_enable"));
			info.setLinkEnable(rs.getBoolean("link_enable"));
			info.setPostEnable(rs.getBoolean("post_enable"));
			info.setNetdiskEnable(rs.getBoolean("netdisk_enable"));
			info.setPublishEnable(rs.getBoolean("publish_enable"));
			info.setTaskEnable(rs.getBoolean("task_enable"));
			info.setAvatarId(rs.getLong("avatar_id"));
			
			info.setEntityCode(rs.getString("entity_code"));
			info.setNodeCode(rs.getString("node_code"));
			info.setInstanceAbbr(rs.getString("abbr"));
			info.setInstanceName(rs.getString("instance_name"));
			info.setInstanceShort(rs.getString("short_name"));
			info.setAdminName(rs.getString("full_name"));
			
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			
			return info;
		}
		
	};
	
	@Override
	public RowMapper<WorkgroupExInfo> getWorkgroupExRowMapper() {
		
		return WorkgroupExMapper;
	}
}
