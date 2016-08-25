package com.gp.svc.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.FlatColumns;
import com.gp.common.IdKey;
import com.gp.common.QuickFlows;
import com.gp.common.ServiceContext;
import com.gp.config.ServiceConfigurer;
import com.gp.dao.ProcFlowDAO;
import com.gp.dao.ProcStepDAO;
import com.gp.dao.PseudoDAO;
import com.gp.dao.QuickFlowDAO;
import com.gp.dao.QuickNodeDAO;
import com.gp.dao.info.ProcFlowInfo;
import com.gp.dao.info.ProcStepInfo;
import com.gp.dao.info.QuickFlowInfo;
import com.gp.dao.info.QuickNodeInfo;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;
import com.gp.svc.QuickFlowService;

@Service("quickflowService")
public class QuickFlowServiceImpl implements QuickFlowService{

	@Autowired
	QuickFlowDAO quickflowdao;
	
	@Autowired
	QuickNodeDAO quicknodedao;
	
	@Autowired
	ProcFlowDAO procflowdao;
	
	@Autowired
	ProcStepDAO procstepdao;
	
	@Autowired
	PseudoDAO pseudodao;
	
	@Autowired
	CommonService idservice;
	
	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public void launchPostPublic(ServiceContext svcctx, String descr,InfoId<Long> wgroupId,  InfoId<Long> postId) throws ServiceException {
		
		// get the quick flow id
		Long fid = pseudodao.query(wgroupId, FlatColumns.PUBLIC_FLOW_ID, Long.class);
		// query flow definition
		QuickFlowInfo finfo = quickflowdao.query(IdKey.QUICK_FLOW.getInfoId(fid));
		// create process flow information
		ProcFlowInfo pinfo = new ProcFlowInfo();
		// generate a new id
		InfoId<Long> procId = idservice.generateId(IdKey.PROC_FLOW, Long.class);
		pinfo.setInfoId(procId);
		pinfo.setFlowId(fid);
		pinfo.setResourceId(postId.getId());
		pinfo.setResourceType(postId.getIdKey());
		pinfo.setDescription(descr);
		pinfo.setOwner(svcctx.getPrincipal().getAccount());
		pinfo.setProcName(finfo.getFlowName());
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		Date now = calendar.getTime();
		pinfo.setLaunchTime(now);
		calendar.add(Calendar.DATE, finfo.getDuration());
		pinfo.setState(QuickFlows.FlowState.START.name());
		pinfo.setExpireTime(calendar.getTime());
		svcctx.setTraceInfo(pinfo);
		
		QuickNodeInfo rootnode = quicknodedao.queryRootNode(IdKey.QUICK_FLOW.getInfoId(fid));
		InfoId<Long> nodeId = idservice.generateId(IdKey.PROC_STEP, Long.class);
		ProcStepInfo sinfo = new ProcStepInfo();
		sinfo.setInfoId(nodeId);
		sinfo.setCreateTime(now);
		sinfo.setProcId(procId.getId());
		sinfo.setNodeId(rootnode.getId());
		sinfo.setPrevStep(QuickFlows.ROOT_NODE);
		sinfo.setState(QuickFlows.StepState.PENDING.name());
		sinfo.setStepName(rootnode.getNodeName());
		
		svcctx.setTraceInfo(sinfo);
		
		
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public void submitPostPublic(ServiceContext svcctx, InfoId<Long> stepId) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

}
