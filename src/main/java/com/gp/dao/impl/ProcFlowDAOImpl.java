package com.gp.dao.impl;

import com.gp.common.FlatColumns;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.ProcFlowDAO;
import com.gp.dao.info.ProcFlowInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.util.CommonUtils;
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
@Component
public class ProcFlowDAOImpl extends DAOSupport implements ProcFlowDAO{

	static Logger LOGGER = LoggerFactory.getLogger(ProcFlowDAOImpl.class);
	
	@Autowired
    public ProcFlowDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource){
        this.setDataSource(dataSource);
    }

    @Override
    public int create(ProcFlowInfo info) {
        StringBuffer SQL = new StringBuffer();

        SQL.append("insert into gp_proc_flows (")
                .append("proc_id, workgroup_id, flow_id, proc_name,")
                .append("descr, owner, launch_time, expire_time,")
                .append("state, json_data, resource_id, resource_type,")
                .append("complete_time,")
                .append("cust_process, modifier, last_modified")
                .append(")values(")
                .append("?,?,?,?,")
                .append("?,?,?,?,")
                .append("?,?,?,?,")
                .append("?,")
                .append("?,?,?)");

        InfoId<Long> key = info.getInfoId();
        String dataStr = CommonUtils.toJson(info.getData());
        Object[] params = new Object[]{
                key.getId(),info.getWorkgroupId(), info.getFlowId(), info.getProcName(),
                info.getDescription(), info.getOwner(), info.getLaunchTime(), info.getExpireTime(),
                info.getState(), dataStr, info.getResourceId(), info.getResourceType(),
                info.getCompleteTime(),
                info.getCustomProcess(),info.getModifier(),info.getModifyDate()
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

        if(columnCheck(mode, colset, "workgroup_id")){
            SQL.append("workgroup_id = ?,");
            params.add(info.getWorkgroupId());
        }
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
        if(columnCheck(mode, colset, "resource_id")){
            SQL.append("resource_id = ? ,");
            params.add(info.getResourceId());
        }
        if(columnCheck(mode, colset, "resource_type")){
            SQL.append("resource_type = ? ,");
            params.add(info.getResourceType());
        }
        if(columnCheck(mode, colset, "cust_process")){
            SQL.append("cust_process = ? ,");
            params.add(info.getCustomProcess());
        }
        if(columnCheck(mode, colset, "json_data")){
            SQL.append("json_data = ? ,");
            String dataStr = CommonUtils.toJson(info.getData());
            params.add(dataStr);
        }
        if(columnCheck(mode, colset, "complete_time")){
            SQL.append("complete_time = ? ,");
            params.add(info.getCompleteTime());
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
