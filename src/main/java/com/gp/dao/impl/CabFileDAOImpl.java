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
import com.gp.dao.CabFileDAO;
import com.gp.dao.info.CabFileInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component("cabFileDAO")
public class CabFileDAOImpl extends DAOSupport implements CabFileDAO{
	
	Logger LOGGER = LoggerFactory.getLogger(CabFileDAOImpl.class);
	
	@Autowired
	public CabFileDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( CabFileInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_cab_files (")
			.append("source_id,cabinet_id,folder_id,file_id,")
			.append("file_name,descr,profile,properties,hash_code,")
			.append("acl_id,size,owner,comment_on,owm,")
			.append("version,version_label,state,binary_id,format,")
			.append("create_time,creator,classification,")
			.append("modifier,last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?,?,")
			.append("?,?,?,?,?,")
			.append("?,?,?,?,?,")
			.append("?,?,?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				info.getSourceId(),info.getCabinetId(),info.getParentId(),key.getId(),
				info.getEntryName(),info.getDescription(),info.getProfile(),info.getProperties(),info.getHashCode(),
				info.getAclId(),info.getSize(),info.getOwner(),info.isCommentOn(),info.getOwm(),
				info.getVersion(),info.getVersionLabel(),info.getState(),info.getBinaryId(),info.getFormat(),
				info.getCreateDate(),info.getCreator(),info.getClassification(),
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
		SQL.append("delete from gp_cab_files ")
			.append("where file_id = ? ");
		
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
	public int update(CabFileInfo info, FilterMode mode, FlatColLocator ...exclcols) {
		Set<String> colset = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_cab_files set ");
		
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
			params.add(info.getEntryName());
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
		if(columnCheck(mode, colset, "hash_code")){
			SQL.append("hash_code=?,");
			params.add(info.getHashCode());
		}
		if(columnCheck(mode, colset, "acl_id")){
			SQL.append("acl_id = ? ,");
			params.add(info.getAclId());
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
		if(columnCheck(mode, colset, "owm")){
			SQL.append("owm = ?,");
			params.add(info.getOwm());
		}
		if(columnCheck(mode, colset, "version")){
			SQL.append("version = ? ,");
			params.add(info.getVersion());
		}
		if(columnCheck(mode, colset, "version_label")){
			SQL.append("version_label=?,");
			params.add(info.getVersionLabel());
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
		if(columnCheck(mode, colset, "classification")){
			SQL.append("classification = ?,");
			params.add(info.getClassification());
		}
	
		SQL.append("modifier = ?, last_modified = ? ")
			.append("where file_id = ? ");
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
	public CabFileInfo query( InfoId<?> id) {
		String SQL = "select * from gp_cab_files "
				+ "where file_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		List<CabFileInfo> ainfo = jtemplate.query(SQL, params, CabFileMapper);
		return ainfo.size()>0 ? ainfo.get(0):null;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}


	@Override
	public List<CabFileInfo> queryByParent(Long parentFolderId) {
		
		String SQL = "select * from gp_cab_files "
				+ "where folder_id = ?";
		
		Object[] params = new Object[]{				
				parentFolderId
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		List<CabFileInfo> ainfo = jtemplate.query(SQL, params, CabFileMapper);
		return ainfo;
	}
}
