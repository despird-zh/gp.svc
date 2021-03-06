package com.gp.svc.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gp.common.Measures;
import com.gp.common.DataSourceHolder;
import com.gp.dao.MeasureDAO;
import com.gp.dao.PseudoDAO;
import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.dao.info.MeasureInfo;
import com.gp.svc.MeasureService;

@Service
public class MeasureServiceImpl implements MeasureService{
	
	static Logger LOGGER = LoggerFactory.getLogger(MeasureServiceImpl.class);
	
	@Autowired
	MeasureDAO measuredao;
	
	@Autowired 
	PseudoDAO pseudodao;

	@Transactional(value = DataSourceHolder.TRNS_MGR, readOnly = true)
	@Override
	public MeasureInfo getWorkgroupLatestSummary(InfoId<Long> wid) throws ServiceException{
		
		try{
			
			MeasureInfo minfo = measuredao.queryLatest(wid, 
					Measures.MEAS_TYPE_WG_SUM, 
					Measures.WG_MEAS_FILE,
					Measures.WG_MEAS_EXT_MBR,
					Measures.WG_MEAS_MEMBER,
					Measures.WG_MEAS_POST,
					Measures.WG_MEAS_SUB_GRP);
			
			return minfo;
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.meas", dae, "workgroup");
		}
	}

	@Override
	public MeasureInfo getNodeLatestSummary(InfoId<Integer> nodeid) throws ServiceException {
		try{
			
			MeasureInfo minfo = measuredao.queryLatest(nodeid, 
					Measures.MEAS_TYPE_NODE_SUM,
					Measures.NODE_MEAS_MEMBER,
					Measures.NODE_MEAS_GROUP,
					Measures.NODE_MEAS_TOPIC,
					Measures.NODE_MEAS_FILE,
					Measures.NODE_MEAS_POINT,
					Measures.NODE_MEAS_EXPERT);
			
			return minfo;
			
		}catch(DataAccessException dae){
			
			throw new ServiceException("excp.meas", dae, "workgroup");
		}
	}
	
	
}
