package com.gp.svc.impl;

import java.util.*;

import com.gp.common.*;
import com.gp.dao.*;
import com.gp.dao.info.*;
import com.gp.info.FlatColLocator;
import com.gp.info.Identifier;

import com.gp.quickflow.FlowProcess;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.DataSourceHolder;
import com.gp.exception.BaseException;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.info.KVPair;
import com.gp.quickflow.FlowProcessFactory;
import com.gp.svc.CommonService;
import com.gp.svc.QuickFlowService;
import com.gp.svc.info.ProcFlowExtInfo;
import com.gp.svc.info.ProcTrailExtInfo;
import com.gp.common.QuickFlows.DefaultExecutor;
import com.gp.common.QuickFlows.ExecMode;
import com.gp.common.QuickFlows.FlowState;
import com.gp.common.QuickFlows.StepOpinion;

@Service
public class QuickFlowServiceImpl implements QuickFlowService{

	static Logger LOGGER = LoggerFactory.getLogger(QuickFlowServiceImpl.class);

	@Autowired
	QuickFlowDAO quickflowdao;
	
	@Autowired
	QuickNodeDAO quicknodedao;
	
	@Autowired
	ProcFlowDAO procflowdao;
	
	@Autowired
	ProcStepDAO procstepdao;

	@Autowired
	ProcTrailDAO proctraildao;

	@Autowired
	PseudoDAO pseudodao;

	@Autowired
	NotificationDAO notifdao;

	@Autowired
	NotificationDispatchDAO notifdispatchdao;

	@Autowired
	CommonService idservice;
	
	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public void launchPostPublic(ServiceContext svcctx, String descr,InfoId<Long> wgroupId,  InfoId<Long> postId) throws ServiceException {

		try{
			// get the quick flow id
			Map<String, Object> wmap = pseudodao.query(wgroupId, FlatColumns.PUBLIC_FLOW_ID,
					FlatColumns.ADMIN,
					FlatColumns.MANAGER);
			long fid = ((Integer)wmap.get(FlatColumns.PUBLIC_FLOW_ID.getColumn())).longValue();
			// query flow definition
			QuickFlowInfo finfo = quickflowdao.query(IdKeys.getInfoId(IdKey.GP_QUICK_FLOWS, fid));
			// create process flow information
			ProcFlowInfo procInfo = new ProcFlowInfo();
			// generate a new id
			InfoId<Long> procId = idservice.generateId(IdKey.GP_PROC_FLOWS, Long.class);
			procInfo.setInfoId(procId);
			procInfo.setFlowId(fid);
			procInfo.setWorkgroupId(wgroupId.getId());
			procInfo.setResourceId(postId.getId());
			procInfo.setResourceType(postId.getIdKey().getSchema());
			procInfo.setDescription(descr);
			procInfo.setOwner(svcctx.getPrincipal().getAccount());
			procInfo.setProcName(finfo.getFlowName());
			procInfo.setCustomProcess(finfo.getCustomProcess());
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			Date now = calendar.getTime();
			procInfo.setLaunchTime(now);
			calendar.add(Calendar.DATE, finfo.getDuration());
			procInfo.setState(QuickFlows.FlowState.START.name());
			procInfo.setExpireTime(calendar.getTime());
			svcctx.setTraceInfo(procInfo);
			procflowdao.create(procInfo);


			// query quick node information : root node
			QuickNodeInfo rootnode = quicknodedao.queryRootNode(IdKeys.getInfoId(IdKey.GP_QUICK_FLOWS,fid));
			Set<String> executors = getStepExecutors(procId, rootnode.getExecutors());
			InfoId<Long> stepId = idservice.generateId(IdKey.GP_PROC_STEP, Long.class);
			ProcStepInfo stepInfo = new ProcStepInfo();
			stepInfo.setInfoId(stepId);
			stepInfo.setCreateTime(now);
			stepInfo.setProcId(procId.getId());
			stepInfo.setNodeId(rootnode.getId());
			stepInfo.setPrevStep(QuickFlows.ROOT_NODE);
			stepInfo.setState(QuickFlows.StepState.PENDING.name());
			stepInfo.setStepName(rootnode.getNodeName());
			stepInfo.setExecutors(executors);
			svcctx.setTraceInfo(stepInfo);
			procstepdao.create(stepInfo);

			// create notification information
			NotificationInfo notifInfo = new NotificationInfo();
			InfoId<Long> notifId = idservice.generateId(IdKey.GP_NOTIFICATIONS, Long.class);
			notifInfo.setInfoId(notifId);
			notifInfo.setSourceId(GeneralConstants.LOCAL_SOURCE);
			notifInfo.setOperation(Operations.LAUNCH_FLOW.name());
			notifInfo.setExcerpt(descr);
			notifInfo.setSender(svcctx.getPrincipal().getAccount());
			notifInfo.setSendTime(now);
			notifInfo.setResourceId(procId.getId());
			notifInfo.setResourceType(procId.getIdKey().getSchema());
			notifInfo.setSubject(finfo.getFlowName());
			notifInfo.setWorkgroupId(wgroupId.getId());
			svcctx.setTraceInfo(notifInfo);
			notifdao.create(notifInfo);

			// Prepare the trail information and notification.
			for(String executor: executors) {

				ProcTrailInfo procTrailInfo = new ProcTrailInfo();
				InfoId<Long> trailId = idservice.generateId(IdKey.GP_PROC_TRAIL, Long.class);
				procTrailInfo.setInfoId(trailId);
				procTrailInfo.setExecutor(executor);
				procTrailInfo.setProcId(procId.getId());
				procTrailInfo.setStepId(stepId.getId());
				svcctx.setTraceInfo(procTrailInfo);
				proctraildao.create(procTrailInfo);

				// create notification dispatch information
				NotificationDispatchInfo notifdisp = new NotificationDispatchInfo();
				InfoId<Long> dispId = idservice.generateId(IdKey.GP_NOTIFICATION_DISPATCH, Long.class);
				notifdisp.setInfoId(dispId);
				notifdisp.setNotificationId(notifId.getId());
				notifdisp.setReceiver(executor);
				svcctx.setTraceInfo(notifdisp);
				notifdispatchdao.create(notifdisp);
			}
		}catch( DataAccessException dae){

			throw new ServiceException("excp.create", dae, "proc flow");
		}
	}

	@Transactional(DataSourceHolder.TRNS_MGR)
	@Override
	public void submitPostPublic(ServiceContext svcctx,InfoId<Long> currStepId, String opinion, String comment) throws ServiceException {
		// find the next node
		QuickNodeInfo nextNode = null;
		
		try{
			ProcStepInfo stepInfo = procstepdao.query(currStepId);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			Date now = calendar.getTime();
			// query the proc data
			InfoId<Long> procId = IdKeys.getInfoId(IdKey.GP_PROC_FLOWS, stepInfo.getProcId());
			ProcFlowInfo procinfo = procflowdao.query(procId);
			// query the step data
			InfoId<Long> currNodeId = IdKeys.getInfoId(IdKey.GP_QUICK_NODE, stepInfo.getNodeId());
			QuickNodeInfo currnode = quicknodedao.query(currNodeId);
			// update the opinion into the trail records.
			proctraildao.updateOpinion(currStepId,
					svcctx.getPrincipal().getAccount(),
					opinion,
					comment);

			ExecMode execmode = ExecMode.valueOf(currnode.getExecMode());
			// prepare the summary of step opinion
			List<KVPair<String, Integer>> cnts = proctraildao.queryOpinionCounts(currStepId);
			int appr_cnt = 0;
			int reject_cnt = 0;
			int abstain_cnt = 0;
			int none_cnt = 0;
			int all_cnt = 0;
			for(KVPair<String, Integer> cnt: cnts){
				if(StepOpinion.APPROVE.name().equals(cnt.getKey())){
					appr_cnt = cnt.getValue(); // approval count
				}else if(StepOpinion.REJECT.name().equals(cnt.getKey())){
					reject_cnt = cnt.getValue(); // reject count
				}else if(StepOpinion.ABSTAIN.name().equals(cnt.getKey())){
					abstain_cnt = cnt.getValue(); // abstain count
				}else{
					none_cnt = cnt.getValue(); // no execute count
				}
				all_cnt += cnt.getValue();
			}

			Map<String, QuickNodeInfo> nextNodeMap = quicknodedao.queryNextNodeMap(currNodeId);

			InfoId<Long> resourceId = IdKeys.getInfoId(procinfo.getResourceType(),procinfo.getResourceId());

			if((execmode == ExecMode.ANYONE_PASS && appr_cnt > 0)
					|| (execmode == ExecMode.ALL_PASS && appr_cnt == all_cnt)){
				// The result : PASS
				nextNode = nextNodeMap.get(QuickFlows.STEP_PASS);

			}else if(execmode == ExecMode.VETO_REJECT && reject_cnt > 0) {
				// The result : FAIL
				nextNode = nextNodeMap.get(QuickFlows.STEP_FAIL);

			}else if(execmode == ExecMode.CUSTOM && none_cnt == 0){
				// all executor complete the execution
				String nextKey = null;

				if(StringUtils.isNotBlank(procinfo.getCustomProcess())){

					FlowProcess customProcessor = FlowProcessFactory.getFlowOperation(procinfo.getCustomProcess());
					if(null != customProcessor && customProcessor.supportCheck(procinfo.getResourceType())){
						nextKey = customProcessor.customNextStep(currStepId, resourceId, procinfo.getData());
					}
				}
				// Find next node By next key
				nextNode = nextNodeMap.get(nextKey);

			}

			// prepare the notification header information
			NotificationInfo notifInfo = new NotificationInfo();
			InfoId<Long> notifId = idservice.generateId(IdKey.GP_NOTIFICATIONS, Long.class);
			notifInfo.setInfoId(notifId);
			notifInfo.setSourceId(GeneralConstants.LOCAL_SOURCE);
			notifInfo.setSender(svcctx.getPrincipal().getAccount());
			notifInfo.setSendTime(now);
			notifInfo.setResourceId(procId.getId());
			notifInfo.setResourceType(procId.getIdKey().getSchema());
			notifInfo.setWorkgroupId(procinfo.getWorkgroupId());
			notifInfo.setSubject(procinfo.getProcName());
			svcctx.setTraceInfo(notifInfo);

			/**
			 * the nextNode not null, means ready to jump to next node
			 **/
			if( null != nextNode){
				// Change the state of current node step
				FlatColLocator[] cols = new FlatColLocator[]{
						FlatColumns.COMPLETE_TIME, FlatColumns.STATE
				};
				Object[] vals = new Object[]{
						new Date(System.currentTimeMillis()), QuickFlows.StepState.COMPLETE.name()
				};
				pseudodao.update(currStepId, cols, vals);

				if(QuickFlows.END_NODE == nextNode.getId()){
					// next node is the ending one, the flow complete
					FlatColLocator[] fcols = new FlatColLocator[]{
							FlatColumns.COMPLETE_TIME, FlatColumns.STATE
					};
					Object[] fvals = new Object[]{
							new Date(System.currentTimeMillis()), QuickFlows.FlowState.END.name()
					};
					pseudodao.update(procId, fcols, fvals);
					if(StringUtils.isNotBlank(procinfo.getCustomProcess())){

						FlowProcess customProcessor = FlowProcessFactory.getFlowOperation(procinfo.getCustomProcess());
						if(null != customProcessor && customProcessor.supportCheck(procinfo.getResourceType())){
							customProcessor.processComplete(procId, resourceId, currnode.getCustomStep(), procinfo.getData());
						}
					}

					// create notification header
					notifInfo.setOperation(Operations.END_FLOW.name());
					notifInfo.setExcerpt("The process ends");
					notifInfo.setQuoteExcerpt("");
					notifdao.create(notifInfo);
					// create notification dispatch to the flow owner
					NotificationDispatchInfo notifdisp = new NotificationDispatchInfo();
					InfoId<Long> dispId = idservice.generateId(IdKey.GP_NOTIFICATION_DISPATCH, Long.class);
					notifdisp.setInfoId(dispId);
					notifdisp.setNotificationId(notifId.getId());
					notifdisp.setReceiver(procinfo.getOwner());
					svcctx.setTraceInfo(notifdisp);
					notifdispatchdao.create(notifdisp);
				}else{
					// ready for jumping to the next node
					Set<String> runners = nextNode.getExecutors();
					// retrieve the real executors
					runners = getStepExecutors(procId, runners);
					InfoId<Long> stepId = idservice.generateId(IdKey.GP_PROC_STEP, Long.class);
					ProcStepInfo nextStepInfo = new ProcStepInfo();
					nextStepInfo.setInfoId(stepId);
					nextStepInfo.setCreateTime(now);
					nextStepInfo.setProcId(stepInfo.getProcId());
					nextStepInfo.setNodeId(nextNode.getId());
					nextStepInfo.setPrevStep(stepInfo.getId());
					nextStepInfo.setState(QuickFlows.StepState.PENDING.name());
					nextStepInfo.setStepName(nextNode.getNodeName());
					// set the executor of step
					nextStepInfo.setExecutors(runners);
					svcctx.setTraceInfo(nextStepInfo);
					procstepdao.create(nextStepInfo);

					// create notification header
					notifInfo.setOperation(Operations.START_STEP.name());
					notifInfo.setExcerpt("start the step:" + nextNode.getNodeName());
					notifInfo.setQuoteExcerpt("");
					notifdao.create(notifInfo);
					for(String executor: runners){

						// create notification dispatch information
						NotificationDispatchInfo notifdisp = new NotificationDispatchInfo();
						InfoId<Long> dispId = idservice.generateId(IdKey.GP_NOTIFICATION_DISPATCH, Long.class);
						notifdisp.setInfoId(dispId);
						notifdisp.setNotificationId(notifId.getId());
						notifdisp.setReceiver(executor);
						svcctx.setTraceInfo(notifdisp);

						notifdispatchdao.create(notifdisp);
					}
				}
			}else if(nextNode == null && none_cnt > 0){

				// create notification header
				notifInfo.setOperation(Operations.SUBMIT_STEP.name());
				notifInfo.setExcerpt("Submit the opinion");
				notifInfo.setQuoteExcerpt("");
				notifdao.create(notifInfo);
				// create notification dispatch to the flow owner
				NotificationDispatchInfo notifdisp = new NotificationDispatchInfo();
				InfoId<Long> dispId = idservice.generateId(IdKey.GP_NOTIFICATION_DISPATCH, Long.class);
				notifdisp.setInfoId(dispId);
				notifdisp.setNotificationId(notifId.getId());
				notifdisp.setReceiver(procinfo.getOwner());
				svcctx.setTraceInfo(notifdisp);
				notifdispatchdao.create(notifdisp);

			}else if(nextNode == null && none_cnt == 0){
				// this is a exception scenario
				LOGGER.warn("Can't find the next node, terminate the process flow.");
				// Change the state of current node step
				FlatColLocator[] cols = new FlatColLocator[]{
						FlatColumns.COMPLETE_TIME, FlatColumns.STATE
				};
				Object[] vals = new Object[]{
						new Date(System.currentTimeMillis()), QuickFlows.StepState.COMPLETE.name()
				};
				pseudodao.update(currStepId, cols, vals);
				// next node is the ending one, the flow complete
				FlatColLocator[] fcols = new FlatColLocator[]{
						FlatColumns.COMPLETE_TIME, FlatColumns.STATE
				};
				Object[] fvals = new Object[]{
						new Date(System.currentTimeMillis()), FlowState.FAIL.name()
				};
				pseudodao.update(procId, fcols, fvals);
				// create notification header
				notifInfo.setOperation(Operations.END_FLOW.name());
				notifInfo.setExcerpt("Terminate the process flow");
				notifInfo.setQuoteExcerpt("");
				notifdao.create(notifInfo);
				// create notification dispatch to the flow owner
				NotificationDispatchInfo notifdisp = new NotificationDispatchInfo();
				InfoId<Long> dispId = idservice.generateId(IdKey.GP_NOTIFICATION_DISPATCH, Long.class);
				notifdisp.setInfoId(dispId);
				notifdisp.setNotificationId(notifId.getId());
				notifdisp.setReceiver(procinfo.getOwner());
				svcctx.setTraceInfo(notifdisp);
				notifdispatchdao.create(notifdisp);
			}

		}catch(DataAccessException | BaseException dae ){
			throw new ServiceException("excp.create", dae, "proc step");
		}
	}

	private Set<String> getStepExecutors(InfoId<Long> procId, Set<String> executorSet) throws ServiceException{
		Set<String> result = new HashSet<>();

		if(CollectionUtils.isEmpty(executorSet))
			return result;

		Long wId = pseudodao.query(procId, FlatColumns.WORKGROUP_ID, Long.class);
		InfoId<Long> wgroupId = IdKeys.getInfoId(IdKey.GP_WORKGROUPS,wId);
		for(String executor: executorSet){
			if(DefaultExecutor.contains(executor)){
				DefaultExecutor runner = DefaultExecutor.valueOf(executor);
				switch (runner){
					case FLOW_OWNER:
						String owner = pseudodao.query(procId, FlatColumns.OWNER, String.class);
						result.add(owner);
						break;
					case WGROUP_ADMIN:
						String admin = pseudodao.query(wgroupId, FlatColumns.ADMIN, String.class);
						result.add(admin);
						break;
					case WGROUP_MANAGER:
						String mgr = pseudodao.query(wgroupId, FlatColumns.MANAGER, String.class);
						result.add(mgr);
						break;
					case FLOW_ATTENDEE:
						List<String> attendees = proctraildao.queryProcAttendees(procId);
						result.addAll(attendees);
						break;
					case RESOURCE_OWNER:
						Map<String,Object> resmap = pseudodao.query(procId, FlatColumns.RESOURCE_ID,FlatColumns.RESOURCE_TYPE);
						if(MapUtils.isNotEmpty(resmap)){
							String res_type = (String)resmap.get(FlatColumns.RESOURCE_TYPE.getColumn());
							Long res_id = (Long)resmap.get(FlatColumns.RESOURCE_ID.getColumn());
							Identifier idf = IdKeys.valueOfIgnoreCase(res_type);
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

	@Override
	public List<QuickNodeInfo> getNodeList(ServiceContext svcctx, InfoId<Long> wgroupId) throws ServiceException {
		
		try{
			Long flowId = pseudodao.query(wgroupId, FlatColumns.PUBLIC_FLOW_ID, Long.class);
			return quicknodedao.queryByFlow(IdKeys.getInfoId(IdKey.GP_QUICK_FLOWS, flowId));
			
		}catch(DataAccessException e){
			throw new ServiceException("excp.query", e, "flow node");
		}
		
	}

	@Override
	public List<ProcFlowExtInfo> getWorkgroupProcs(ServiceContext svcctx, InfoId<Long> workgroupId, String state)
			throws ServiceException {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("SELECT p.*, w.workgroup_name, u.full_name ");
		SQL.append("FROM gp_proc_flows p ");
		SQL.append("LEFT JOIN gp_workgroups w ON (p.workgroup_id = w.workgroup_id) ");
		SQL.append("LEFT JOIN gp_users u ON (p.owner = u.account) ");
		SQL.append("WHERE p.workgroup_id = :workgroup_id ");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("wgroup_id", workgroupId.getId());
		
		if(StringUtils.isNotBlank(state)){
			SQL.append(" AND p.state = :state ");
			params.put("state", state);
		}
		
		SQL.append("ORDER BY p.workgroup_id ASC");
		
		NamedParameterJdbcTemplate jtemplate = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
        }
        
        List<ProcFlowExtInfo> ainfos = jtemplate.query(SQL.toString(), params, PROC_EXT_MAPPER);
        return ainfos;
	}

	@Override
	public List<ProcTrailExtInfo> getWorkgroupProcTrails(ServiceContext svcctx, InfoId<Long> stepId)
			throws ServiceException {
		
		StringBuffer SQL = new StringBuffer();
		SQL.append("select ");
		SQL.append("c.source_id,");
		SQL.append("e.source_name,");
		SQL.append("b.workgroup_id,");
		SQL.append("c.workgroup_name,");
		SQL.append("d.full_name,");
		SQL.append("a.* ");
		SQL.append("from gp_proc_trail a ");
		SQL.append("left join gp_proc_flows b on (a.proc_id = b.proc_id) ");
		SQL.append("left join gp_workgroups c on (b.workgroup_id = c.workgroup_id) ");
		SQL.append("left join gp_sources e on (c.source_id = e.source_id) ");
		SQL.append("left join gp_users d on (a.executor = d.account) ");
		SQL.append("where a.step_id = ?");
		
		Object[] params = new Object[]{
				stepId.getId()
		};
		
		JdbcTemplate jtemplate = pseudodao.getJdbcTemplate(JdbcTemplate.class);
		if(LOGGER.isDebugEnabled()){
            LOGGER.debug("SQL : " + SQL.toString() + " / params : " + ArrayUtils.toString(params));
        }
		
		return jtemplate.query(SQL.toString(), params, TRAIL_EXT_MAPPER);
	}
}
