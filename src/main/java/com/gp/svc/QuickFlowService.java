package com.gp.svc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.gp.common.IdKey;
import com.gp.common.ServiceContext;
import com.gp.dao.info.ProcFlowInfo;
import com.gp.dao.info.QuickNodeInfo;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.svc.info.ProcFlowExtInfo;
import com.gp.util.CommonUtils;

/**
 * Created by garydiao on 8/19/16.
 */
public interface QuickFlowService {

    /**
     * Launch a quick flow for workgroup post publish
     **/
    public void launchPostPublic(ServiceContext svcctx, String descr,InfoId<Long> wgroupId, InfoId<Long> postId) throws ServiceException;

    /**
     * submit a step for workgroup post public
     **/
    public void submitPostPublic(ServiceContext svcctx,
                                 InfoId<Long> currStepId,
                                 String opinion,
                                 String comment) throws ServiceException;
    
    /**
     * Get all the nodes of flow bind to workgroup 
     **/
    public List<QuickNodeInfo> getNodeList(ServiceContext svcctx, InfoId<Long> wgroupId) throws ServiceException;
    
    /**
     * Get all the processes of workgroup 
     **/
    public List<ProcFlowExtInfo> getWorkgroupProcs(ServiceContext svcctx,
            InfoId<Long> currStepId,
            String state) throws ServiceException;
    
    public static RowMapper<ProcFlowExtInfo> PROC_EXT_MAPPER = new RowMapper<ProcFlowExtInfo>() {
        @Override
        public ProcFlowExtInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        	ProcFlowExtInfo info = new ProcFlowExtInfo();

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
            info.setData(CommonUtils.toMap(jsonStr, Object.class));
            info.setCustomProcess(rs.getString("cust_process"));
            info.setCompleteTime(rs.getTimestamp("complete_time"));

            info.setResourceId(rs.getLong("resource_id"));
            info.setResourceType(rs.getString("resource_type"));
            info.setModifier(rs.getString("modifier"));
            info.setModifyDate(rs.getTimestamp("last_modified"));
            
            info.setWorkgroupName(rs.getString("workgroup_name"));
            info.setOwner(rs.getString("full_name"));
            
            return info;
        }
    };
}
