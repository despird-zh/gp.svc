package com.gp.dao;

import com.gp.common.IdKey;
import com.gp.dao.info.QuickNodeInfo;
import com.gp.info.InfoId;
import com.gp.util.CommonUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * Created by garydiao on 8/19/16.
 */
public interface QuickNodeDAO extends BaseDAO<QuickNodeInfo>{

    static RowMapper<QuickNodeInfo> QUICK_NODE_ROWMAPPER = new RowMapper<QuickNodeInfo>() {
        @Override
        public QuickNodeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            QuickNodeInfo info = new QuickNodeInfo();
            InfoId<Long> nid = IdKey.QUICK_NODE.getInfoId(rs.getLong("node_id"));
            info.setInfoId(nid);
            info.setNodeName(rs.getString("node_name"));
            info.setFlowId(rs.getLong("flow_id"));
            info.setExecMode(rs.getString("exec_mode"));
            Set<Long> prevNodes = CommonUtils.toSet(rs.getString("prev_nodes"), Long.class);
            Set<Long> nextNodes = CommonUtils.toSet(rs.getString("next_nodes"), Long.class);

            info.setPrevNodes(prevNodes);
            info.setNextNodes(nextNodes);

            info.setModifier(rs.getString("modifier"));
            info.setModifyDate(rs.getTimestamp("last_modified"));
            return info;
        }
    };
}