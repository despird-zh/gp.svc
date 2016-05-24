package com.gp.svc;

import com.gp.exception.ServiceException;
import com.gp.info.InfoId;
import com.gp.info.MeasureInfo;

public interface MeasureService {

	public MeasureInfo getWorkgroupLatestSummary(InfoId<Long> wid) throws ServiceException;
}
