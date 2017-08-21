package com.gp.dao;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.dao.info.ProcFlowInfo;
import com.gp.info.InfoId;
import com.gp.util.CommonUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by garydiao on 8/19/16.
 */
public interface ProcFlowDAO extends BaseDAO<ProcFlowInfo>{

	/**
	 * Query the work flow process by workgroup and state 
	 * 
	 * @param wgroupId the id of work group
	 * @param state the state of process flow
	 * 
	 **/
	public List<ProcFlowInfo> query(InfoId<Long> wgroupId, String state);
	
    public static RowMapper<ProcFlowInfo> PROC_FLOW_ROWMAPPER = new RowMapper<ProcFlowInfo>() {
        @Override
        public ProcFlowInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProcFlowInfo info = new ProcFlowInfo();

            InfoId<Long> id = IdKeys.getInfoId(IdKey.PROC_FLOW, rs.getLong("proc_id"));
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
            info.setData(CommonUtils.toMap(jsonStr, Object.class));
            info.setCustomProcess(rs.getString("cust_process"));
            info.setCompleteTime(rs.getTimestamp("complete_time"));

            info.setResourceId(rs.getLong("resource_id"));
            info.setResourceType(rs.getString("resource_type"));
            info.setModifier(rs.getString("modifier"));
            info.setModifyDate(rs.getTimestamp("last_modified"));
            return info;
        }
    };
}
