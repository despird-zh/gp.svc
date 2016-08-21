package com.gp.svc;

import com.gp.common.ServiceContext;
import com.gp.dao.info.ProcFlowInfo;
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
    public void submitPostPublic(ServiceContext svcctx, InfoId<Long> stepId) throws ServiceException;
}
