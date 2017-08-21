package com.gp.dao;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.common.QuickFlows;
import com.gp.dao.info.QuickNodeInfo;
import com.gp.info.InfoId;
import com.gp.util.CommonUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by garydiao on 8/19/16.
 */
public interface QuickNodeDAO extends BaseDAO<QuickNodeInfo>{

    public static final QuickNodeInfo END_NODE_INFO = new QuickNodeInfo(IdKeys.getInfoId(IdKey.QUICK_NODE,QuickFlows.END_NODE));

    /**
     * Query the root node i.e the start node
     **/
	public QuickNodeInfo queryRootNode(InfoId<Long> flowId);

    /**
     * Query the key - node map of next nodes
     **/
	public Map<String, QuickNodeInfo> queryNextNodeMap(InfoId<Long> nodeId);
	
	/**
	 * Query the nodes of a flow 
	 **/
	public List<QuickNodeInfo> queryByFlow(InfoId<Long> flowId);
	
    static RowMapper<QuickNodeInfo> QuickNodeMapper = new RowMapper<QuickNodeInfo>() {
        @Override
        public QuickNodeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            QuickNodeInfo info = new QuickNodeInfo();
            InfoId<Long> nid = IdKeys.getInfoId(IdKey.QUICK_NODE,rs.getLong("node_id"));
            info.setInfoId(nid);

            info.setNodeName(rs.getString("node_name"));
            info.setFlowId(rs.getLong("flow_id"));
            info.setExecMode(rs.getString("exec_mode"));

            Set<Long> prevNodes = CommonUtils.toSet(rs.getString("prev_nodes"), Long.class);
            info.setPrevNodes(prevNodes);

            Map<String,Long> nextNodeMap = CommonUtils.toMap(rs.getString("next_node_map"), Long.class);
            info.setNextNodeMap(nextNodeMap);

            Set<String> executor = CommonUtils.toSet(rs.getString("executors"), String.class);
            info.setExecutors(executor);

            info.setCustomStep(rs.getString("cust_step"));

            info.setModifier(rs.getString("modifier"));
            info.setModifyDate(rs.getTimestamp("last_modified"));
            return info;
        }
    };
}
