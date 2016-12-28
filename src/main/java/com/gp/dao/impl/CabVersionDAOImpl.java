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
import com.gp.dao.CabVersionDAO;
import com.gp.dao.info.CabVersionInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component
public class CabVersionDAOImpl extends DAOSupport implements CabVersionDAO{

	Logger LOGGER = LoggerFactory.getLogger(CabVersionDAOImpl.class);
	
	@Autowired
	public CabVersionDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( CabVersionInfo info) {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_cab_versions (")
			.append("source_id,cabinet_id,folder_id,file_id,version_id,")
			.append("file_name,descr,profile,properties,owm,")
			.append("version_label,size,owner,comment_on,")
			.append("version,state,binary_id,format,")
			.append("create_time,creator,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,?,?,?,")
			.append("?,?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		
		Object[] params = new Object[]{
				info.getSourceId(),info.getCabinetId(),info.getParentId(),info.getFileId(),key.getId(),
				info.getFileName(),info.getDescription(),info.getProfile(),info.getProperties(),info.getOwm(),
				info.getVersionLabel(),info.getSize(),info.getOwner(),info.isCommentOn(),
				info.getVersion(),info.getState(),info.getBinaryId(),info.getFormat(),
				info.getCreateDate(),info.getCreator(),
				info.getModifier(),info.getModifyDate()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		return jtemplate.update(SQL.toString(),params);

	}

	@Override
	public int delete( InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_cab_versions ")
			.append("where version_id = ? ");
		
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
	public int update(CabVersionInfo info, FilterMode mode,FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_cab_versions set ");
		
		if(columnCheck(mode, colset, "cabinet_id")){
			SQL.append("cabinet_id = ? ,");
			params.add(info.getCabinetId());
		}
		if(columnCheck(mode, colset, "folder_id")){
			SQL.append("folder_id = ? ,");
			params.add(info.getParentId());
		}
		if(columnCheck(mode, colset, "source_id")){
			SQL.append("source_id = ? ,");
			params.add(info.getSourceId());
		}
		if(columnCheck(mode, colset, "file_name")){
			SQL.append("file_name = ? ,");
			params.add(info.getFileId());
		}
		if(columnCheck(mode, colset, "descr")){
			SQL.append("descr = ? ,");
			params.add(info.getDescription());
		}
		if(columnCheck(mode, colset, "profile")){
			SQL.append("profile = ? ,");
			params.add(info.getProfile());
		}
		if(columnCheck(mode, colset, "properties")){
			SQL.append("properties = ? ,");
			params.add(info.getProperties());
		}
		if(columnCheck(mode, colset, "version_label")){
			SQL.append("version_label = ? ,");
			params.add(info.getVersionLabel());
		}
		if(columnCheck(mode, colset, "size")){
			SQL.append("size = ? ,");
			params.add(info.getSize());
		}
		if(columnCheck(mode, colset, "owner")){
			SQL.append("owner = ? ,");
			params.add(info.getOwner());
		}
		if(columnCheck(mode, colset, "comment_on")){
			SQL.append("comment_on = ? ,");
			params.add(info.isCommentOn());
		}
		if(columnCheck(mode, colset, "version")){
			SQL.append("version = ? ,");
			params.add(info.getVersion());
		}
		if(columnCheck(mode, colset, "state")){
			SQL.append("state = ? ,");
			params.add(info.getState());
		}
		if(columnCheck(mode, colset, "binary_id")){
			SQL.append("binary_id = ? ,");
			params.add(info.getBinaryId());
		}
		if(columnCheck(mode, colset, "format")){
			SQL.append("format = ? ,");
			params.add(info.getFormat());
		}
		if(columnCheck(mode, colset, "create_time")){
			SQL.append("create_time = ? ,");
			params.add(info.getCreateDate());
		}
		if(columnCheck(mode, colset, "creator")){
			SQL.append("creator = ? ,");
			params.add(info.getCreator());
		}
		if(columnCheck(mode, colset, "file_id")){
			SQL.append("file_id = ?, ");
			params.add(info.getFileId());
		}
		if(columnCheck(mode, colset, "owm")){
			SQL.append("owm=?,");
			params.add(info.getOwm());
		}
		SQL.append("modifier = ?, last_modified = ? ");
		SQL.append("where version_id = ? ");
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
	public CabVersionInfo query( InfoId<?> id) {
		String SQL = "select * from gp_cab_versions "
				+ "where version_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<CabVersionInfo> ainfo = jtemplate.query(SQL, params, CabVersionMapper);
		return ainfo.size()>0 ? ainfo.get(0):null;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}


	@Override
	public List<CabVersionInfo> queryByFileId(Long fileid) {
		
		StringBuffer SQL_BUF = new StringBuffer("Select * from gp_cab_versions where file_id = ?");
		
		Object[] params = new Object[]{				
				fileid
			};
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL_BUF.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		List<CabVersionInfo> ainfo = jtemplate.query(SQL_BUF.toString(), params, CabVersionMapper);
		return ainfo;
	}
}
