package com.gp.dao.impl;

import com.gp.common.FlatColumns;
import com.gp.common.QuickFlows.StepOpinion;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.ProcStepDAO;
import com.gp.dao.info.ProcStepInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.KVPair;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by garydiao on 8/19/16.
 */
@Component
public class ProcStepDAOImpl extends DAOSupport implements ProcStepDAO{

	static Logger LOGGER = LoggerFactory.getLogger(ProcStepDAOImpl.class);
	
	@Autowired
    public ProcStepDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource){
        this.setDataSource(dataSource);
    }

    @Override
    public int create(ProcStepInfo info) {
        StringBuffer SQL = new StringBuffer();

        SQL.append("insert into gp_proc_step (")
                .append("step_id, proc_id, node_id, step_name,")
                .append("prev_step, create_time, exec_time,")
                .append("state, opinion, executor, comment,")
                .append("operation,modifier, last_modified,")
                .append(")values(")
                .append("?,?,?,?,")
                .append("?,?,?,?,")
                .append("?,?,?,?,")
                .append("?,? ");

        InfoId<Long> key = info.getInfoId();
        Object[] params = new Object[]{
                key.getId(),info.getProcId(), info.getNodeId(), info.getStepName(),
                info.getPrevStep(), info.getCreateTime(), info.getExecuteTime(),
                info.getState(), info.getOpinion(), info.getExecutor(), info.getComment(),
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
        SQL.append("delete from gp_proc_step ")
                .append("where step_id = ? ");

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
    public int update(ProcStepInfo info, FlatColumns.FilterMode mode, FlatColLocator... filterCols) {
        Set<String> colset = FlatColumns.toColumnSet(filterCols);
        List<Object> params = new ArrayList<Object>();
        StringBuffer SQL = new StringBuffer();
        SQL.append("update gp_proc_step set ");

        if(columnCheck(mode, colset, "proc_id")){
            SQL.append("proc_id = ?,");
            params.add(info.getProcId());
        }
        if(columnCheck(mode, colset, "node_id")){
            SQL.append("node_id =?,");
            params.add(info.getNodeId());
        }
        if(columnCheck(mode, colset, "step_name")){
            SQL.append("step_name = ? ,");
            params.add(info.getStepName());
        }
        if(columnCheck(mode, colset, "prev_step")){
            SQL.append("prev_step = ? ,");
            params.add(info.getPrevStep());
        }
    
        if(columnCheck(mode, colset, "create_time")){
            SQL.append("create_time = ? ,");
            params.add(info.getCreateTime());
        }

        if(columnCheck(mode, colset, "exec_time")){
            SQL.append("exec_time = ? ,");
            params.add(info.getExecuteTime());
        }
        if(columnCheck(mode, colset, "state")){
            SQL.append("state = ? ,");
            params.add(info.getState());
        }
        if(columnCheck(mode, colset, "opinion")){
            SQL.append("opinion = ? ,");
            params.add(info.getOpinion());
        }

        if(columnCheck(mode, colset, "comment")){
            SQL.append("comment = ? ,");
            params.add(info.getComment());
        }
        
        SQL.append("modifier = ?, last_modified = ? ")
                .append("where step_id = ? ");
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
    public ProcStepInfo query(InfoId<?> id) {
        String SQL = "select * from gp_proc_step "
                + "where step_id = ? ";

        Object[] params = new Object[]{
                id.getId()
        };

        JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
        }
        List<ProcStepInfo> ainfo = jtemplate.query(SQL, params, ProcStepRowMapper);
        return ainfo.size()>0 ? ainfo.get(0) : null;
    }

    @Override
    protected void initialJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	public List<String> queryProcAttendees(InfoId<Long> procId) {
		String SQL = "select executor from gp_proc_step "
                + "where proc_id = ? ";

        Object[] params = new Object[]{
                procId.getId()
        };

        JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
        }
        List<String> ainfo = jtemplate.queryForList(SQL, String.class, params);
        return ainfo;
	}

	@Override
	public List<KVPair<String, Integer>> queryStepStateCounts(InfoId<Long> procId) {
		String SQL = "select opinion,count(opinion) as cnt from gp_proc_step "
                + "where proc_id = ? group by opinion";

        Object[] params = new Object[]{
                procId.getId()
        };

        JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
        }
        List<KVPair<String, Integer>> kvs = jtemplate.query(SQL, params, new RowMapper<KVPair<String, Integer>>(){

			@Override
			public KVPair<String, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
				KVPair<String, Integer> kv = new KVPair<String, Integer>();
				String k = rs.getString("opinion");
				k = StringUtils.isBlank(k)? StepOpinion.NONE.name() : k;
				
				kv.setKey(k);
				kv.setValue(rs.getInt("cnt"));
				return kv;
			}});
        return kvs;
	}
}
