package com.gp.svc.impl;

import java.util.*;

import com.gp.common.*;
import com.gp.dao.*;
import com.gp.dao.info.*;
import com.gp.info.FlatColLocator;
import com.gp.info.Identifier;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.config.ServiceConfigurer;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.info.KVPair;
import com.gp.svc.CommonService;
import com.gp.svc.QuickFlowService;
import com.gp.common.QuickFlows.DefaultExecutor;
import com.gp.common.QuickFlows.ExecMode;
import com.gp.common.QuickFlows.FlowState;
import com.gp.common.QuickFlows.StepOpinion;

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
	NotificationDAO notifdao;

	@Autowired
	NotificationDispatchDAO notifdispatchdao;

	@Autowired
	CommonService idservice;
	
	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public void launchPostPublic(ServiceContext svcctx, String descr,InfoId<Long> wgroupId,  InfoId<Long> postId) throws ServiceException {
		
		// get the quick flow id
		Map<String, Object> wmap = pseudodao.query(wgroupId, FlatColumns.PUBLIC_FLOW_ID,
				FlatColumns.ADMIN,
				FlatColumns.MANAGER);
		long fid = (Long)wmap.get(FlatColumns.PUBLIC_FLOW_ID.getColumn());
		// query flow definition
		QuickFlowInfo finfo = quickflowdao.query(IdKey.QUICK_FLOW.getInfoId(fid));
		// create process flow information
		ProcFlowInfo pinfo = new ProcFlowInfo();
		// generate a new id
		InfoId<Long> procId = idservice.generateId(IdKey.PROC_FLOW, Long.class);
		pinfo.setInfoId(procId);
		pinfo.setFlowId(fid);
		pinfo.setWorkgroupId(wgroupId.getId());
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

		// query quick node information : root node
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
		// set the executor of step
		sinfo.setExecutor((String)wmap.get(FlatColumns.ADMIN.getColumn()));
		svcctx.setTraceInfo(sinfo);

		// create notification information
		NotificationInfo notifInfo = new NotificationInfo();
		InfoId<Long> notifId = idservice.generateId(IdKey.NOTIF, Long.class);
		notifInfo.setInfoId(notifId);
		notifInfo.setOperation(Operations.LAUNCH_FLOW.name());
		notifInfo.setExcerpt(descr);
		notifInfo.setSender(svcctx.getPrincipal().getAccount());
		notifInfo.setSendTime(now);
		notifInfo.setResourceId(procId.getId());
		notifInfo.setResourceType(procId.getIdKey());
		notifInfo.setSubject(finfo.getFlowName());
		notifInfo.setWorkgroupId(wgroupId.getId());
		svcctx.setTraceInfo(notifInfo);
		// create notification dispatch information
		NotificationDispatchInfo notifdisp = new NotificationDispatchInfo();
		InfoId<Long> dispId = idservice.generateId(IdKey.NOTIF_DISPATCH, Long.class);
		notifdisp.setInfoId(dispId);
		notifdisp.setNotificationId(notifId.getId());
		notifdisp.setReceiver((String)wmap.get(FlatColumns.ADMIN.getColumn()));
		svcctx.setTraceInfo(notifdisp);

		// commit the table information
		try{
			procflowdao.create(pinfo);
			procstepdao.create(sinfo);
			notifdao.create(notifInfo);
			notifdispatchdao.create(notifdisp);

		}catch( DataAccessException dae){

			throw new ServiceException("excp.create", dae, "proc flow");
		}
	}

	@Transactional(ServiceConfigurer.TRNS_MGR)
	@Override
	public void submitPostPublic(ServiceContext svcctx,InfoId<Long> currStepId, InfoId<Long> nextNodeId, String opinion, String comment) throws ServiceException {
		// find the next node
		QuickNodeInfo nextnode = quicknodedao.query(nextNodeId);
		ProcStepInfo stepinfo = procstepdao.query(currStepId);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		Date now = calendar.getTime();
		InfoId<Long> procId = IdKey.PROC_FLOW.getInfoId(stepinfo.getProcId());
		ProcFlowInfo procinfo = procflowdao.query(procId);
		try{
			ExecMode execmode = ExecMode.valueOf(nextnode.getExecMode());
			List<KVPair<String, Integer>> cnts = procstepdao.queryStepStateCounts(procId);
			int appr_cnt = 0;
			int reject_cnt = 0;
			int all_cnt = 0;
			for(KVPair<String, Integer> cnt: cnts){
				if(StepOpinion.APPROVE.name().equals(cnt.getKey())){
					appr_cnt = cnt.getValue();
				}else if(StepOpinion.REJECT.name().equals(cnt.getKey())){
					reject_cnt = cnt.getValue();
				}
				all_cnt += cnt.getValue();
			}
			// create notification information
			NotificationInfo notifInfo = new NotificationInfo();
			InfoId<Long> notifId = idservice.generateId(IdKey.NOTIF, Long.class);
			notifInfo.setInfoId(notifId);
			notifInfo.setOperation(Operations.SUBMIT_STEP.name());
			notifInfo.setQuoteExcerpt(procinfo.getDescription());
			notifInfo.setExcerpt(comment);
			notifInfo.setSender(svcctx.getPrincipal().getAccount());
			notifInfo.setSendTime(now);
			notifInfo.setResourceId(procId.getId());
			notifInfo.setResourceType(procId.getIdKey());
			notifInfo.setSubject(procinfo.getProcName());
			notifInfo.setWorkgroupId(procinfo.getWorkgroupId());
			svcctx.setTraceInfo(notifInfo);
			notifdao.create(notifInfo);
			// not the ending node
			if(!nextnode.getNextNodes().contains(QuickFlows.END_NODE)){
				
				if((execmode == ExecMode.ANYONE_PASS && appr_cnt > 0)
					|| (execmode == ExecMode.VETO_REJECT && reject_cnt > 0)
					|| (execmode == ExecMode.ALL_PASS && appr_cnt == all_cnt)){
	
					Set<String> runners = nextnode.getExecutor();
					// retrieve the real executors
					runners = getStepExecutors(procId, runners);
					for(String executor: runners){
						
						InfoId<Long> stepId = idservice.generateId(IdKey.PROC_STEP, Long.class);
						ProcStepInfo sinfo = new ProcStepInfo();
						
						sinfo.setInfoId(stepId);
						sinfo.setCreateTime(now);
						sinfo.setProcId(stepinfo.getProcId());
						sinfo.setNodeId(nextNodeId.getId());
						sinfo.setPrevStep(stepinfo.getId());
						sinfo.setState(QuickFlows.StepState.PENDING.name());
						sinfo.setStepName(nextnode.getNodeName());
	
						// set the executor of step
						sinfo.setExecutor(executor);
						svcctx.setTraceInfo(sinfo);						
						procstepdao.create(sinfo);
						
						// create notification dispatch information
						NotificationDispatchInfo notifdisp = new NotificationDispatchInfo();
						InfoId<Long> dispId = idservice.generateId(IdKey.NOTIF_DISPATCH, Long.class);
						notifdisp.setInfoId(dispId);
						notifdisp.setNotificationId(notifId.getId());
						notifdisp.setReceiver(executor);
						svcctx.setTraceInfo(notifdisp);
						
						notifdispatchdao.create(notifdisp);
					}
				}
				
			}else{
				// change the state of process
				pseudodao.update(procId, FlatColumns.STATE, FlowState.END.name());
			}
			// end node not need further processing
			FlatColLocator[] cols = new FlatColLocator[]{FlatColumns.OPINION, FlatColumns.COMMENT, FlatColumns.NEXT_STEP};
			Object[] vals = new Object[]{opinion, comment};

			pseudodao.update(currStepId, cols, vals);

		}catch(DataAccessException dae){
			throw new ServiceException("excp.create", dae, "proc step");
		}
	}

	private Set<String> getStepExecutors(InfoId<Long> procId, Set<String> executorSet) throws ServiceException{
		Set<String> result = new HashSet<>();

		if(CollectionUtils.isEmpty(executorSet))
			return result;

		for(String executor: executorSet){
			if(DefaultExecutor.contains(executor)){
				DefaultExecutor runner = DefaultExecutor.valueOf(executor);
				switch (runner){
					case FLOW_OWNER:
						String owner = pseudodao.query(procId, FlatColumns.OWNER, String.class);
						result.add(owner);
						break;
					case WGROUP_ADMIN:
						String admin = pseudodao.query(procId, FlatColumns.ADMIN, String.class);
						result.add(admin);
						break;
					case WGROUP_MANAGER:
						String mgr = pseudodao.query(procId, FlatColumns.MANAGER, String.class);
						result.add(mgr);
						break;
					case FLOW_ATTENDEE:
						List<String> attendees = procstepdao.queryProcAttendees(procId);
						result.addAll(attendees);
						break;
					case RESOURCE_OWNER:
						Map<String,Object> resmap = pseudodao.query(procId, FlatColumns.RESOURCE_ID,FlatColumns.RESOURCE_TYPE);
						if(MapUtils.isNotEmpty(resmap)){
							String res_type = (String)resmap.get(FlatColumns.RESOURCE_TYPE.getColumn());
							Long res_id = (Long)resmap.get(FlatColumns.RESOURCE_ID.getColumn());
							Identifier idf = IdKey.valueOfIgnoreCase(res_type);
							InfoId<Long> resId = new InfoId<Long>(idf, res_id);
							
							String res_owner = pseudodao.query(resId, FlatColumns.OWNER, String.class);
							result.add(res_owner);
						}
						break;
					default:
						break;
				}

			}else{
				result.add(executor);
			}
		}

		return result;
	}
}
