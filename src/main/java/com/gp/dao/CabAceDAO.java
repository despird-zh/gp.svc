package com.gp.dao;

import java.util.List;

import com.gp.info.CabAceInfo;

public interface CabAceDAO extends BaseDAO<CabAceInfo>{

	public CabAceInfo queryBySubject(Long aclid,String type, String subject);
	
	public int deleteByAcl(Long aclid);
	
	public int deleteBySubject(Long aclid, String type,String subject);
	
	public List<CabAceInfo> queryByAcl(Long aclid);
}
