package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.gp.common.FlatColumns;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.AttachDAO;
import com.gp.dao.info.AttachInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component("attachDAO")
public class AttachDAOImpl extends DAOSupport implements AttachDAO{

	Logger LOGGER = LoggerFactory.getLogger(AttachDAOImpl.class);
	
	@Autowired
	public AttachDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(AttachInfo info){

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_attachments (")
			.append("attachment_id, resource_id,resource_type,")
			.append("attachment_name,size,owner,state,")
			.append("binary_id,format,modifier, last_modified,")
			.append("hash_code,owm,create_time,creator")
			.append(")values(")
			.append("?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?)");
		
		InfoId<Long> key = info.getInfoId();
		
		Object[] params = new Object[]{
				key.getId(),info.getResourceId(),info.getResourceType(),
				info.getAttachName(),info.getSize(),info.getOwner(),info.getState(),
				info.getBinaryId(),info.getFormat(),info.getModifier(),info.getModifyDate(),
				info.getHashCode(),info.getOwm(),info.getCreateDate(),info.getCreator()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		int cnt = jtemplate.update(SQL.toString(),params);
		return cnt;
	}

	@Override
	public int delete(InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_attachments ")
			.append("where attachment_id = ? ");
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		Object[] params = new Object[]{
			id.getId()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(), params);
		return rtv;
	}

	@Override
	public int update(AttachInfo info,FilterMode mode, FlatColLocator ...cols) {

		Set<String> colset = FlatColumns.toColumnSet(cols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_attachments set ");
		
		if(columnCheck(mode, colset, "resource_type")){
			SQL.append("resource_type = ?,");
			params.add(info.getResourceType());
		}
		if(columnCheck(mode, colset, "attachment_name")){
			SQL.append("attachment_name =?,");
			params.add(info.getAttachName());
		}
		if(columnCheck(mode, colset, "resource_id")){
			SQL.append("resource_id = ? ,");
			params.add(info.getResourceId());
		}
		if(columnCheck(mode, colset, "size")){
			SQL.append("size = ?,");
			params.add(info.getSize());
		}
		if(columnCheck(mode, colset, "owner")){
			SQL.append("owner = ? , ");
			params.add(info.getOwner());
		}
		if(columnCheck(mode, colset, "state")){
			SQL.append("state = ?,");
			params.add(info.getState());
		}
		if(columnCheck(mode, colset, "binary_id")){
			SQL.append("binary_id=?,");
			params.add(info.getBinaryId());
		}
		if(columnCheck(mode, colset, "hash_code")){
			SQL.append("hash_code= ?,");
			params.add(info.getHashCode());
		}
		if(columnCheck(mode, colset, "owm")){
			SQL.append(" owm = ?,");
			params.add(info.getOwm());
		}
		if(columnCheck(mode, colset, "format")){
			SQL.append(" format = ?,"); 
			params.add(info.getFormat());
		}
		if(columnCheck(mode, colset, "creator")){
			SQL.append("creator=?,"); 
			params.add(info.getCreator());
		}
		if(columnCheck(mode, colset, "create_time")){
			SQL.append("create_time=? ,");
			params.add(info.getCreateDate());
		}
		
		SQL.append("modifier = ?, last_modified = ? ")
			.append("where attachment_id = ? ");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());

		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params.toArray());
		return rtv;
	}

	@Override
	public AttachInfo query( InfoId<?> id) {
		String SQL = "select * from gp_attachments "
				+ "where attachment_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<AttachInfo> ainfo = jtemplate.query(SQL, params, AttachMapper);
		return ainfo.size()>0 ? ainfo.get(0):null;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
