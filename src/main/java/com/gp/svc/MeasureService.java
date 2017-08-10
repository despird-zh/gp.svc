package com.gp.svc;

import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.dao.info.MeasureInfo;

public interface MeasureService {

	public MeasureInfo getWorkgroupLatestSummary(InfoId<Long> wid) throws ServiceException;
	
	public MeasureInfo getNodeLatestSummary(InfoId<Integer> nodeid) throws ServiceException;
	
}
