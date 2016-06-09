package com.gp.svc;

import java.util.List;

import com.gp.acl.Ace;
import com.gp.acl.Acl;
import com.gp.common.ServiceContext;
import com.gp.exception.ServiceException;
import com.gp.info.CabAceInfo;
import com.gp.info.InfoId;

public interface AclService {

	public InfoId<Long> addAclInfo(ServiceContext svcctx, List<CabAceInfo> aces) throws ServiceException;
	
	public void addAceInfos(ServiceContext svcctx, InfoId<Long> aclId, CabAceInfo ...ace) throws ServiceException;
	
	public InfoId<Long> addAceInfo(ServiceContext svcctx, InfoId<Long> aclId, CabAceInfo ace) throws ServiceException;
	
	public CabAceInfo getAceInfo(ServiceContext svcctx, InfoId<Long> aclId,String aceType, String subject) throws ServiceException;
	
	public Acl getAcl(ServiceContext svcctx, InfoId<Long> aclId) throws ServiceException;
	
	public Ace getAce(ServiceContext svcctx, InfoId<Long> aclId,String aceType, String subject) throws ServiceException;

	public boolean removeAcl(ServiceContext svcctx, InfoId<Long> aclId) throws ServiceException;
	
	public boolean removeAce(ServiceContext svcctx, InfoId<Long> aclId,String aceType, String subject) throws ServiceException;
	
}
