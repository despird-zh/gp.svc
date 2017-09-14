package com.gp.dao.impl;

import com.gp.common.FlatColumns;
import com.gp.common.QuickFlows;
import com.gp.common.DataSourceHolder;
import com.gp.dao.ProcTrailDAO;
import com.gp.dao.info.ProcTrailInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.KVPair;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by garydiao on 10/4/16.
 */
@Component
public class ProcTrailDAOImpl extends DAOSupport implements ProcTrailDAO{

    @Autowired
    public ProcTrailDAOImpl(@Qualifier(DataSourceHolder.DATA_SRC)DataSource dataSource){

        this.setDataSource(dataSource);
    }

    @Override
    public int create(ProcTrailInfo info) {
        StringBuffer SQL = new StringBuffer();

        SQL.append("insert into gp_proc_trail (")
                .append("trail_id, proc_id, step_id, opinion,")
                .append("comment, executor, execute_time, ")
                .append("modifier, last_modified ")
                .append(")values(")
                .append("?,?,?,?,")
                .append("?,?,?,")
                .append("?,? )");

        InfoId<Long> key = info.getInfoId();
        Object[] params = new Object[]{
                key.getId(),info.getProcId(), info.getStepId(), info.getOpinion(),
                info.getComment(), info.getExecutor(), info.getExecuteTime(),
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
        SQL.append("delete from gp_proc_trail ")
                .append("where trail_id = ? ");

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
    public int update(ProcTrailInfo info, FlatColumns.FilterMode mode, FlatColLocator... filterCols) {
        Set<String> colset = FlatColumns.toColumnSet(filterCols);
        List<Object> params = new ArrayList<Object>();
        StringBuffer SQL = new StringBuffer();
        SQL.append("update gp_proc_trail set ");

        if(columnCheck(mode, colset, "proc_id")){
            SQL.append("proc_id = ?,");
            params.add(info.getProcId());
        }
        if(columnCheck(mode, colset, "step_id")){
            SQL.append("step_id =?,");
            params.add(info.getStepId());
        }
        if(columnCheck(mode, colset, "opinion")){
            SQL.append("opinion = ? ,");
            params.add(info.getOpinion());
        }
        if(columnCheck(mode, colset, "comment")){
            SQL.append("comment = ? ,");
            params.add(info.getComment());
        }

        if(columnCheck(mode, colset, "executor")){
            SQL.append("executor = ? ,");
            params.add(info.getExecutor());
        }

        if(columnCheck(mode, colset, "execute_time")){
            SQL.append("execute_time = ? ,");
            params.add(info.getExecuteTime());
        }

        SQL.append("modifier = ?, last_modified = ? ")
                .append("where trail_id = ? ");
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
    public ProcTrailInfo query(InfoId<?> id) {
        String SQL = "select * from gp_proc_trail "
                + "where trail_id = ? ";

        Object[] params = new Object[]{
                id.getId()
        };

        JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
        }
        List<ProcTrailInfo> ainfo = jtemplate.query(SQL, params, TrailMapper);
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
    public List<KVPair<String, Integer>> queryOpinionCounts(InfoId<Long> stepId) {
        String SQL = "select opinion,count(opinion) as cnt from gp_proc_trail "
                + "where step_id = ? group by opinion";

        Object[] params = new Object[]{
                stepId.getId()
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
                k = StringUtils.isBlank(k)? QuickFlows.StepOpinion.NONE.name() : k;

                kv.setKey(k);
                kv.setValue(rs.getInt("cnt"));
                return kv;
            }});
        return kvs;
    }

    @Override
    public int updateOpinion(InfoId<Long> stepId, String executor, String opinion, String comment) {
        StringBuffer SQL = new StringBuffer("UPDATE gp_proc_trail SET ");
        SQL.append("opinion = ?, comment = ?, last_modified = ?, modifier = ? ");
        SQL.append("WHERE step_id = ? AND executor = ? ");

        Object[] params = new Object[]{
             opinion, comment, new Date(System.currentTimeMillis()), executor,
                stepId.getId(), executor
        };
        JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
        }
        int rtv = jtemplate.update(SQL.toString(), params);
        return rtv;
    }
}
