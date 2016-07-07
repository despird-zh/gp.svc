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
import com.gp.config.ServiceConfigurer;
import com.gp.dao.SysOptionDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.SysOptionInfo;

@Component("sysOptionDAO")
public class SysOptionDAOImpl extends DAOSupport implements SysOptionDAO{

	Logger LOGGER = LoggerFactory.getLogger(SysOptionDAOImpl.class);
	
	@Autowired
	public SysOptionDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create( SysOptionInfo info) {

		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_sys_options (")
			.append("sys_opt_id,opt_group,")
			.append("opt_key,opt_value,descr,")
			.append("modifier, last_modified")
			.append(")values(")
			.append("?,?,")
			.append("?,?,?,")
			.append("?,?)");
		
		Object[] params = new Object[]{
				info.getInfoId().getId(),info.getOptionGroup(),
				info.getOptionKey(),info.getOptionValue(),info.getDescription(),
				info.getModifier(),info.getModifyDate()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}

		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		return jtemplate.update(SQL.toString(),params);

	}

	@Override
	public int delete( InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_sys_options ")
			.append("where sys_opt_id = ?");
		
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
	public int update( SysOptionInfo info, FlatColLocator ...exclcols) {
		Set<String> cols = FlatColumns.toColumnSet(exclcols);
		List<Object> params = new ArrayList<Object>();

		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_sys_options set ");
		if(!cols.contains("opt_group")){
			SQL.append("opt_group = ?,");
			params.add(info.getOptionGroup());
		}
		if(!cols.contains("opt_key")){
			SQL.append("opt_key = ?,");
			params.add(info.getOptionKey());
		}
		if(!cols.contains("opt_value")){
			SQL.append("opt_value = ? , ");
			params.add(info.getOptionValue());
		}
		if(!cols.contains("descr")){
			SQL.append("descr = ?,");
			params.add(info.getDescription());
		}
		
		SQL.append("modifier = ?, last_modified = ? ")
			.append("where sys_opt_id = ?");
		params.add(info.getModifier());
		params.add(info.getModifyDate());
		params.add(info.getInfoId().getId());
		
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		
		int rtv = -1;
		rtv = jtemplate.update(SQL.toString(), params.toArray());

		return rtv;
	}

	@Override
	public SysOptionInfo query( InfoId<?> id) {
		String SQL = "select * from gp_sys_options "
				+ "where sys_opt_id = ?";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		SysOptionInfo ainfo = null;

		ainfo = jtemplate.queryForObject(SQL, params, SysOptionMapper);

		return ainfo;
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}


	@Override
	public List<SysOptionInfo> queryAll() {
		
		String SQL = "select * from gp_sys_options ";
		
		List<SysOptionInfo> options = null;
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() );
		}

			options = jtemplate.query(SQL, SysOptionMapper);

		return options;
	}

	@Override
	public List<SysOptionInfo> queryByGroup( String groupKey) {
		
		String SQL = "select * from gp_sys_options where opt_group = ?";
		
		List<SysOptionInfo> options = null;
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		
		Object[] params = new Object[]{				
				groupKey
			};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}

		options = jtemplate.query(SQL, SysOptionMapper);

		return options;
	}

	@Override
	public SysOptionInfo queryByKey( String optKey){
		String SQL = "select * from gp_sys_options where opt_key = ?";

		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		
		Object[] params = new Object[]{				
				optKey
			};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		SysOptionInfo option = null;

		option = jtemplate.queryForObject(SQL, params, SysOptionMapper);

		return option;
	}

	@Override
	public int updateByKey(String optionKey, String optionValue) {
		
		String SQL = "update gp_sys_options set opt_value = ? where opt_key = ?";

		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
		
		Object[] params = new Object[]{				
				optionValue, optionKey
			};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		
		return jtemplate.update(SQL, params);

	}
}
