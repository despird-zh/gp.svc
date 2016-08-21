package com.gp.dao.impl;

import com.gp.common.FlatColumns;
import com.gp.common.QuickFlowNodes;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.PseudoDAO;
import com.gp.dao.QuickNodeDAO;
import com.gp.dao.info.QuickNodeInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.util.CommonUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by garydiao on 8/19/16.
 */
@Component("quicknodeDAO")
public class QuickNodeDAOImpl extends DAOSupport implements QuickNodeDAO{

	static Logger LOGGER = LoggerFactory.getLogger(QuickNodeDAOImpl.class);
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Autowired
    public QuickNodeDAOImpl(@Qualifier(ServiceConfigurer.DATA_SRC)DataSource dataSource){
        this.setDataSource(dataSource);
    }

    @Override
    public int create(QuickNodeInfo info) {

        StringBuffer SQL = new StringBuffer();

        SQL.append("insert into gp_quick_node (")
                .append("node_id, flow_id, node_name,")
                .append("prev_nodes, next_nodes, exec_mode,")
                .append("modifier, last_modified,")
                .append(")values(")
                .append("?,?,?,")
                .append("?,?,?,")
                .append("?,? ");

        InfoId<Long> key = info.getInfoId();
        String prevSteps = CommonUtils.toJson(info.getPrevNodes());
        String nextSteps = CommonUtils.toJson(info.getNextNodes());
        Object[] params = new Object[]{
                key.getId(),info.getFlowId(), info.getNodeName(),
                prevSteps, nextSteps, info.getExecMode(),
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
        SQL.append("delete from gp_quick_node ")
                .append("where node_id = ? ");

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
    public int update(QuickNodeInfo info, FlatColumns.FilterMode mode, FlatColLocator... filterCols) {
        Set<String> colset = FlatColumns.toColumnSet(filterCols);
        List<Object> params = new ArrayList<Object>();
        StringBuffer SQL = new StringBuffer();
        SQL.append("update gp_quick_node set ");

        if(columnCheck(mode, colset, "node_name")){
            SQL.append("node_name = ?,");
            params.add(info.getNodeName());
        }
        if(columnCheck(mode, colset, "flow_id")){
            SQL.append("flow_id =?,");
            params.add(info.getFlowId());
        }
        if(columnCheck(mode, colset, "prev_nodes")){
            SQL.append("prev_nodes = ? ,");
            String prevNodes = CommonUtils.toJson(info.getPrevNodes());
            params.add(prevNodes);
        }
        if(columnCheck(mode, colset, "next_nodes")){
            SQL.append("next_nodes = ? ,");
            String nextNodes = CommonUtils.toJson(info.getNextNodes());
            params.add(nextNodes);
        }

        SQL.append("modifier = ?, last_modified = ? ")
                .append("where node_id = ? ");
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
    public QuickNodeInfo query(InfoId<?> id) {
        String SQL = "select * from gp_quick_node "
                + "where node_id = ? ";

        Object[] params = new Object[]{
                id.getId()
        };

        JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
        }
        List<QuickNodeInfo> ainfo = jtemplate.query(SQL, params, QUICK_NODE_ROWMAPPER);
        return ainfo.size()>0 ? ainfo.get(0) : null;
    }

    @Override
    protected void initialJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	public QuickNodeInfo queryRootNode(InfoId<Long> flowId) {
		String SQL = "select * from gp_quick_node "
                + "where flow_id = ? and prev_nodes = [" + QuickFlowNodes.ROOT_NODE + "]";

        Object[] params = new Object[]{
                flowId.getId()
        };

        JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
        }
        List<QuickNodeInfo> ainfo = jtemplate.query(SQL, params, QUICK_NODE_ROWMAPPER);
        return ainfo.size()>0 ? ainfo.get(0) : null;
	}

	@Override
	public QuickNodeInfo queryEndNode(InfoId<Long> flowId) {
		String SQL = "select * from gp_quick_node "
                + "where flow_id = ? and prev_nodes = [" + QuickFlowNodes.END_NODE + "]";

        Object[] params = new Object[]{
                flowId.getId()
        };

        JdbcTemplate jtemplate = this.getJdbcTemplate(JdbcTemplate.class);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
        }
        List<QuickNodeInfo> ainfo = jtemplate.query(SQL, params, QUICK_NODE_ROWMAPPER);
        return ainfo.size()>0 ? ainfo.get(0) : null;
	}

	@Override
	public List<QuickNodeInfo> queryPrevNodes(InfoId<Long> nodeId) {
		String nodes_json = pseudodao.query(nodeId, FlatColumns.PREV_NODES, String.class);
		if(StringUtils.isBlank(nodes_json) || StringUtils.equals(nodes_json, "[]")){
			return null;
		}else{
			List<Long> ids = CommonUtils.toList(nodes_json, Long.class);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("nodes", ids);
			String SQL = "select * from gp_quick_nodes where node_id in (:nodes)";
			NamedParameterJdbcTemplate jtemplate = getJdbcTemplate(NamedParameterJdbcTemplate.class);
	        if(LOGGER.isDebugEnabled()){
	            LOGGER.debug("SQL : " + SQL + " / params : " + params.toString());
	        }
	        List<QuickNodeInfo> ainfos = jtemplate.query(SQL, params, QUICK_NODE_ROWMAPPER);
	        return ainfos;
		}
	}

	@Override
	public List<QuickNodeInfo> queryNextNodes(InfoId<Long> nodeId) {
		
		String nodes_json = pseudodao.query(nodeId, FlatColumns.NEXT_NODES, String.class);
		if(StringUtils.isBlank(nodes_json) || StringUtils.equals(nodes_json, "[]")){
			return null;
		}else{
			List<Long> ids = CommonUtils.toList(nodes_json, Long.class);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("nodes", ids);
			String SQL = "select * from gp_quick_nodes where node_id in (:nodes)";
			NamedParameterJdbcTemplate jtemplate = getJdbcTemplate(NamedParameterJdbcTemplate.class);
	        if(LOGGER.isDebugEnabled()){
	            LOGGER.debug("SQL : " + SQL + " / params : " + params.toString());
	        }
	        List<QuickNodeInfo> ainfos = jtemplate.query(SQL, params, QUICK_NODE_ROWMAPPER);
	        return ainfos;
		}
	}
}
