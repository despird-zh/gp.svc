package com.gp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.gp.common.IdKey;
import com.gp.config.ServiceConfigurator;
import com.gp.dao.DictionaryDAO;
import com.gp.info.DictionaryInfo;
import com.gp.info.InfoId;

@Component("dictionaryDAO")
public class DictionaryDAOImpl extends DAOSupport implements DictionaryDAO{

	Logger LOGGER = LoggerFactory.getLogger(DictionaryDAOImpl.class);
	
	@Autowired
	public DictionaryDAOImpl(@Qualifier(ServiceConfigurator.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( DictionaryInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_dictionary (")
			.append("dict_id,dict_group,")
			.append("dict_key,dict_value,language,label,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,")
			.append("?,?,?,?,")
			.append("?,?)");
		
		Object[] params = new Object[]{
				info.getInfoId().getId(),info.getGroup(),
				info.getKey(),info.getValue(),info.getLanguage(),info.getLabel(),
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
		SQL.append("delete from gp_dictionary ")
			.append("where dict_id = ?");
		
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
	public int update(DictionaryInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_dictionary set ")
			.append("dict_group = ?,")
			.append("dict_key = ?,dict_value = ?,label = ?,language=?,")
			.append("modifier = ?, last_modified = ? ")
			.append("where dict_id = ?");
		
		Object[] params = new Object[]{
				info.getGroup(),
				info.getKey(),info.getValue(),info.getLabel(),info.getLanguage(),
				info.getModifier(),info.getModifyDate(),
				info.getInfoId().getId()
		};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		int rtv = jtemplate.update(SQL.toString(),params);
		return rtv;
	}

	@Override
	public DictionaryInfo query( InfoId<?> id) {
		String SQL = "select * from gp_dictionary "
				+ "where dict_id = ?";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		DictionaryInfo ainfo = jtemplate.queryForObject(SQL, params, DictionaryMapper);
		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);		
	}

	public static RowMapper<DictionaryInfo> DictionaryMapper = new RowMapper<DictionaryInfo>(){

		@Override
		public DictionaryInfo mapRow(ResultSet rs, int arg1) throws SQLException {
			DictionaryInfo info = new DictionaryInfo();
			InfoId<Long> id = IdKey.DICTIONARY.getInfoId(rs.getLong("dict_id"));
			info.setInfoId(id);

			info.setGroup(rs.getString("dict_group"));
			info.setKey(rs.getString("dict_key"));
			info.setValue(rs.getString("dict_value"));
			info.setLabel(rs.getString("label"));
			info.setLanguage(rs.getString("language"));
			info.setModifier(rs.getString("modifier"));
			info.setModifyDate(rs.getTimestamp("last_modified"));
			return info;
		}
	};
	
	@Override
	public RowMapper<DictionaryInfo> getRowMapper() {
		
		return DictionaryMapper;
	}
}
