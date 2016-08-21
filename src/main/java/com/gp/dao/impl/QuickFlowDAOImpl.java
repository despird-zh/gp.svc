package com.gp.dao.impl;

import com.gp.common.FlatColumns;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.QuickFlowDAO;
import com.gp.dao.info.QuickFlowInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by garydiao on 8/19/16.
 */
@Component("quickflowDAO")
public class QuickFlowDAOImpl extends DAOSupport implements QuickFlowDAO{

	static Logger LOGGER = LoggerFactory.getLogger(QuickFlowDAOImpl.class);
	
	@Autowired
    public QuickFlowDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource){
        this.setDataSource(dataSource);
    }

    @Override
    public int create(QuickFlowInfo info) {
        StringBuffer SQL = new StringBuffer();
        SQL.append("insert into gp_quick_flows (")
                .append("flow_id, flow_name, category,")
                .append("duration,")
                .append("modifier, last_modified,")
                .append(")values(")
                .append("?,?,?,")
                .append("?,")
                .append("?,?");

        InfoId<Long> key = info.getInfoId();

        Object[] params = new Object[]{
                key.getId(),info.getFlowName(),info.getCategory(),
                info.getDuration(),
                info.getModifier(),info.getModifyDate()
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
        SQL.append("delete from gp_quick_flows ")
                .append("where flow_id = ? ");

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
    public int update(QuickFlowInfo info, FlatColumns.FilterMode mode, FlatColLocator... cols) {
        Set<String> colset = FlatColumns.toColumnSet(cols);
        List<Object> params = new ArrayList<Object>();
        StringBuffer SQL = new StringBuffer();
        SQL.append("update gp_quick_flows set ");

        if(columnCheck(mode, colset, "flow_name")){
            SQL.append("flow_name = ?,");
            params.add(info.getFlowName());
        }
        if(columnCheck(mode, colset, "category")){
            SQL.append("category =?,");
            params.add(info.getCategory());
        }
        if(columnCheck(mode, colset, "duration")){
            SQL.append("duration = ? ,");
            params.add(info.getDuration());
        }


        SQL.append("modifier = ?, last_modified = ? ")
                .append("where flow_id = ? ");
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
    public QuickFlowInfo query(InfoId<?> id) {

        String SQL = "select * from gp_quick_flows "
                + "where flow_id = ? ";

        Object[] params = new Object[]{
                id.getId()
        };

        JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
        }
        List<QuickFlowInfo> ainfo = jtemplate.query(SQL, params, QUICK_FLOW_ROWMAPPER);
        return ainfo.size()>0 ? ainfo.get(0) : null;
    }

    @Override
    protected void initialJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
