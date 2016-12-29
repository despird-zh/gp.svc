package com.gp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
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
import com.gp.dao.TokenDAO;
import com.gp.dao.info.TokenInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;

@Component
public class TokenDAOImpl extends DAOSupport implements TokenDAO{

	Logger LOGGER = LoggerFactory.getLogger(TokenDAOImpl.class);
	
	@Autowired
	public TokenDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public int create(TokenInfo info) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("insert into gp_tokens (")
			.append("token_id, issuer, audience, expire_time,")
			.append("not_before, subject, issue_at, jwt_token,")
			.append("claims,")
			.append("modifier,last_modified")
			.append(")values(")
			.append("?,?,?,?,")
			.append("?,?,?,?,")
			.append("?,")
			.append("?,?)");
		
		InfoId<Long> key = info.getInfoId();
		Object[] params = new Object[]{
				key.getId(),info.getIssuer(), info.getAudience(), info.getExpireTime(),
				info.getNotBefore(), info.getSubject(), info.getIssueAt(), info.getJwtToken(),
				info.getClaims()
				,info.getModifier(),info.getModifyDate()
		};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);

			// execute sql
		return jtemplate.update(SQL.toString(),params);
	}

	@Override
	public int delete(InfoId<?> id) {
		StringBuffer SQL = new StringBuffer();
		SQL.append("delete from gp_tokens ")
			.append("where token_id = ? ");
		
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
	public int update(TokenInfo info, FilterMode mode, FlatColLocator... filterCols) {
		
		Set<String> colset = FlatColumns.toColumnSet(filterCols);
		List<Object> params = new ArrayList<Object>();

		StringBuffer SQL = new StringBuffer();
		SQL.append("update gp_tokens set ");
		
		if(columnCheck(mode, colset, "issuer")){
			SQL.append("issuer = ?,");
			params.add(info.getIssuer());
		}
		if(columnCheck(mode, colset, "audience")){
			SQL.append("audience = ?,");
			params.add(info.getAudience());
		}
		if(columnCheck(mode, colset, "expire_time")){
			SQL.append("expire_time = ? ,");
			params.add(info.getExpireTime());
		}
		if(columnCheck(mode, colset, "not_before")){
			SQL.append("not_before = ?,");
			params.add(info.getNotBefore());
		}
		if(columnCheck(mode, colset, "subject")){
			SQL.append("subject = ?,");
			params.add(info.getSubject());
		}
		if(columnCheck(mode, colset, "issue_at")){
			SQL.append("issue_at = ?,");
			params.add(info.getIssueAt());
		}
		if(columnCheck(mode, colset, "jwt_token")){
			SQL.append("jwt_token = ?,");
			params.add(info.getJwtToken());
		}
		if(columnCheck(mode, colset, "claims")){
			SQL.append("claims = ?, ");
			params.add(info.getClaims());
		}
		
		SQL.append("modifier = ?,last_modified = ? ")
		.append("where token_id = ? ");
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
	public TokenInfo query(InfoId<?> id) {
		String SQL = "select * from gp_tokens "
				+ "where token_id = ? ";
		
		Object[] params = new Object[]{				
				id.getId()
			};
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
		}
		JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);

		List<TokenInfo>	ainfo = jtemplate.query(SQL, params, TokenMapper);

		return CollectionUtils.isEmpty(ainfo) ? null : ainfo.get(0);
	}

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
