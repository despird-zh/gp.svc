package com.gp.svc;

import java.util.List;

import com.gp.common.ServiceContext;
import com.gp.dao.info.ProcFlowInfo;
import com.gp.dao.info.QuickNodeInfo;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;

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
}
