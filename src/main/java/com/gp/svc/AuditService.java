package com.gp.svc;

import java.sql.Date;
import java.util.List;

import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.dao.info.AuditInfo;
import com.gp.info.InfoId;

public interface AuditService {

	public List<AuditInfo> getAudits(ServiceContext svcctx, String subject, String object, String operation) throws ServiceException;
	
	public boolean deleteAudit(ServiceContext svcctx, InfoId<Long> id ) throws ServiceException;
	
	public boolean addAudit(ServiceContext svcctx, AuditInfo ainfo ) throws ServiceException;
	
	public boolean purgeAudits(ServiceContext svcctx, String subject, String objectType, Date reservedate) throws ServiceException;
}
