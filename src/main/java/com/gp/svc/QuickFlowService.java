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
     * Launch a quick flow
     **/
    public void launchProcFlow(ServiceContext svcctx, ProcFlowInfo flowInfo) throws ServiceException;

    public void completeProcStep(ServiceContext svcctx, InfoId<Long> stepId) throws ServiceException;
}
