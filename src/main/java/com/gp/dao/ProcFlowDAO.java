package com.gp.dao;

import com.gp.common.IdKey;
import com.gp.dao.info.ProcFlowInfo;
import com.gp.dao.info.QuickFlowInfo;
import com.gp.info.InfoId;
import com.gp.util.CommonUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by garydiao on 8/19/16.
 */
public interface ProcFlowDAO extends BaseDAO<ProcFlowInfo>{

    public static RowMapper<ProcFlowInfo> PROC_FLOW_ROWMAPPER = new RowMapper<ProcFlowInfo>() {
        @Override
        public ProcFlowInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProcFlowInfo info = new ProcFlowInfo();

            InfoId<Long> id = IdKey.PROC_FLOW.getInfoId(rs.getLong("proc_id"));
            info.setInfoId(id);

            info.setWorkgroupId(rs.getLong("workgroup_id"));
            info.setFlowId(rs.getLong("flow_id"));
            info.setProcName(rs.getString("proc_name"));
            info.setDescription(rs.getString("descr"));
            info.setOwner(rs.getString("owner"));
            info.setLaunchTime(rs.getTimestamp("launch_time"));
            info.setExpireTime(rs.getTimestamp("expire_time"));
            info.setState(rs.getString("state"));
            String jsonStr = rs.getString("json_data");
            info.setData(CommonUtils.toMap(jsonStr));

            info.setResourceId(rs.getLong("resource_id"));
            info.setResourceType(rs.getString("resource_type"));
            info.setModifier(rs.getString("modifier"));
            info.setModifyDate(rs.getTimestamp("last_modified"));
            return info;
        }
    };
}
