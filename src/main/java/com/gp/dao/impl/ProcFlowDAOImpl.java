package com.gp.dao.impl;

import com.gp.common.FlatColumns;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.ProcFlowDAO;
import com.gp.dao.info.ProcFlowInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.util.CommonUtils;
import org.apache.commons.lang.ArrayUtils;
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
@Component("procflowDAO")
public class ProcFlowDAOImpl extends DAOSupport implements ProcFlowDAO{

    public ProcFlowDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource){
        this.setDataSource(dataSource);
    }

    @Override
    public int create(ProcFlowInfo info) {
        StringBuffer SQL = new StringBuffer();

        SQL.append("insert into gp_proc_flows (")
                .append("proc_id, flow_id, proc_name,")
                .append("descr, owner, launch_time, expire_time,")
                .append("state, json_data,")
                .append("modifier, last_modified,")
                .append(")values(")
                .append("?,?,?,")
                .append("?,?,?,?,")
                .append("?,?,")
                .append("?,? ");

        InfoId<Long> key = info.getInfoId();
        String dataStr = CommonUtils.toJson(info.getData());
        Object[] params = new Object[]{
                key.getId(),info.getFlowId(), info.getProcName(),
                info.getDescription(), info.getOwner(), info.getLaunchTime(), info.getExpireTime(),
                info.getState(), dataStr,
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
        SQL.append("delete from gp_proc_flows ")
                .append("where proc_id = ? ");

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
    public int update(ProcFlowInfo info, FlatColumns.FilterMode mode, FlatColLocator... filterCols) {
        Set<String> colset = FlatColumns.toColumnSet(filterCols);
        List<Object> params = new ArrayList<Object>();
        StringBuffer SQL = new StringBuffer();
        SQL.append("update gp_proc_flows set ");

        if(columnCheck(mode, colset, "flow_id")){
            SQL.append("flow_id = ?,");
            params.add(info.getFlowId());
        }
        if(columnCheck(mode, colset, "proc_name")){
            SQL.append("proc_name =?,");
            params.add(info.getProcName());
        }
        if(columnCheck(mode, colset, "descr")){
            SQL.append("descr = ? ,");
            params.add(info.getDescription());
        }
        if(columnCheck(mode, colset, "launch_time")){
            SQL.append("launch_time = ? ,");
            params.add(info.getLaunchTime());
        }
        if(columnCheck(mode, colset, "expire_time")){
            SQL.append("expire_time = ? ,");
            params.add(info.getExpireTime());
        }

        if(columnCheck(mode, colset, "json_data")){
            SQL.append("json_data = ? ,");
            String dataStr = CommonUtils.toJson(info.getData());
            params.add(dataStr);
        }

        SQL.append("modifier = ?, last_modified = ? ")
                .append("where proc_id = ? ");
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
    public ProcFlowInfo query(InfoId<?> id) {
        String SQL = "select * from gp_proc_flows "
                + "where proc_id = ? ";

        Object[] params = new Object[]{
                id.getId()
        };

        JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
        }
        List<ProcFlowInfo> ainfo = jtemplate.query(SQL, params, PROC_FLOW_ROWMAPPER);
        return ainfo.size()>0 ? ainfo.get(0) : null;
    }

    @Override
    protected void initialJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
