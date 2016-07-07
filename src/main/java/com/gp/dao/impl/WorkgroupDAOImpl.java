package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.WorkgroupDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
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
			.append("source_id,workgroup_id,workgroup_name,hash_code,storage_id,manager,")
			.append("descr,state,admin,creator,org_id,mbr_group_id,")
			.append("publish_cab_id, netdisk_cab_id,owm,publish_enable,task_enable,workgroup_pid,")
			.append("share_enable, link_enable,post_enable,netdisk_enable,avatar_id,mbr_post_acpt,")
			.append("create_time,modifier, last_modified")
			.append(")values(")
			.append("?,?,?,?,?,?,")
			.append("?,?,?,?,?,?,")
			.append("?,?,?,?,?,?,")
			.append("?,?,?,?,?,?,")
			.append("?,?,?)");

		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				info.getSourceId(),key.getId(),info.getWorkgroupName(),info.getHashCode(),info.getStorageId(),info.getManager(),
				info.getDescription(),info.getState(),info.getAdmin(),info.getCreator(),info.getOrgId(),info.getMemberGroupId(),
				info.getPublishCabinet(),info.getNetdiskCabinet(),info.getOwm(),info.getPublishEnable(),info.getTaskEnable(),info.getParentId(),
				info.getShareEnable(),info.getLinkEnable(),info.getPostEnable(),info.getNetdiskEnable(),info.getAvatarId(),info.getPostAcceptable(),
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
	public int update( WorkgroupInfo info, FlatColLocator ...exclcols) {
		Set<String> cols = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();

		StringBuffer SQL = new StringBuffer();
		SQL.append("UPDATE gp_workgroups SET ");
		if(!cols.contains("workgroup_name")){
			SQL.append("workgroup_name = ?,");
			params.add(info.getWorkgroupName());
		}
		if(!cols.contains("org_id")){
			SQL.append("org_id = ?,");
			params.add(info.getOrgId());
		}
		if(!cols.contains("storage_id")){
			SQL.append("storage_id = ?,");
			params.add(info.getStorageId());
		}
		if(!cols.contains("source_id")){
			SQL.append("source_id = ?, ");
			params.add(info.getSourceId());
		}
		if(!cols.contains("mbr_group_id")){
			SQL.append("mbr_group_id = ?,");
			params.add(info.getMemberGroupId());
		}
		if(!cols.contains("descr")){
			SQL.append("descr = ?,");
			params.add(info.getDescription());
		}
		if(!cols.contains("state")){
			SQL.append("state = ? ,");
			params.add(info.getState());
		}
		if(!cols.contains("admin")){
			SQL.append("admin = ?,");
			params.add(info.getAdmin());
		}
		if(!cols.contains("creator")){
			SQL.append("creator = ?,");
			params.add(info.getCreator());
		}
		if(!cols.contains("hash_code")){
			SQL.append("hash_code = ?, ");
			params.add(info.getHashCode());
		}
		if(!cols.contains("manager")){
			SQL.append("manager=?, ");
			params.add(info.getManager());
		}
		if(!cols.contains("create_time")){
			SQL.append("create_time = ?,");
			params.add(info.getCreateDate());
		}
		if(!cols.contains("avatar_id")){
			SQL.append("avatar_id = ?,");
			params.add(info.getAvatarId());
		}
		if(!cols.contains("workgroup_pid")){
			SQL.append("workgroup_pid=?,");
			params.add(info.getParentId());
		}
		if(!cols.contains("publish_cab_id")){
			SQL.append("publish_cab_id = ?,");
			params.add(info.getPublishCabinet());
		}
		if(!cols.contains("netdisk_cab_id")){
			SQL.append("netdisk_cab_id = ? ,");
			params.add(info.getNetdiskCabinet());
		}
		if(!cols.contains("owm")){
			SQL.append("owm = ? ,");
			params.add(info.getOwm());
		}
		if(!cols.contains("publish_enable")){
			SQL.append("publish_enable = ? ,");
			params.add(info.getPublishEnable());
		}
		if(!cols.contains("task_enable")){
			SQL.append("task_enable = ? ,");
			params.add(info.getTaskEnable());
		}
		if(!cols.contains("share_enable")){
			SQL.append("share_enable = ? , ");
			params.add(info.getShareEnable());
		}
		if(!cols.contains("link_enable")){
			SQL.append("link_enable = ? ,");
			params.add(info.getLinkEnable());
		}
		if(!cols.contains("post_enable")){
			SQL.append("post_enable = ? ,");
			params.add(info.getPostEnable());
		}
		if(!cols.contains("netdisk_enable")){
			SQL.append("netdisk_enable = ? ");
			params.add(info.getNetdiskEnable());
		}
		if(!cols.contains("mbr_post_acpt")){
			SQL.append("mbr_post_acpt = ?,");
			params.add(info.getPostAcceptable());
		}
		
		SQL.append("modifier = ?, last_modified = ?,")
			.append("WHERE workgroup_id = ?  ");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());

		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int rtv = -1;
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		rtv = jtemplate.update(SQL.toString(), params.toArray());

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


	@Override
	public List<WorkgroupInfo> queryByIds(InfoId<?>... ids) {
		
		List<Long> oids = new ArrayList<Long>();
		
		for(InfoId<?> id : ids){
			oids.add((Long)id.getId());
		}
		StringBuffer SQL = new StringBuffer("SELECT * FROM gp_workgroups ");
		SQL.append("where workgroup_id IN (:wgroup_ids)");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("wgroup_ids", oids);
		
		NamedParameterJdbcTemplate jtemplate = super.getJdbcTemplate(NamedParameterJdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL + " / params : " + oids.toString());
		}
		
		List<WorkgroupInfo> infos = jtemplate.query(SQL.toString(), params, WorkgroupMapper);
		
		return infos;
	}

}
