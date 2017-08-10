package gp.svc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.config.TestConfig;
import com.gp.acl.Ace;
import com.gp.acl.AcePrivilege;
import com.gp.acl.AceType;
import com.gp.acl.Acl;
import com.gp.common.GeneralConstants;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.common.GroupUsers;
import com.gp.common.IdKey;
import com.gp.dao.info.CabAceInfo;
import com.gp.info.InfoId;
import com.gp.svc.AclService;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class AclTest extends AbstractJUnit4SpringContextTests{
	
	Principal principal = GroupUsers.PSEUDO_USER;
	ServiceContext svcctx ;
	
	@Autowired
    private CommonService idService;
	
	@Autowired
    private AclService aclService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);

		List<CabAceInfo> acelist = new ArrayList<CabAceInfo>();

		CabAceInfo aceowner = new CabAceInfo();
		aceowner.setSubjectType(AceType.OWNER.value);
		aceowner.setSubject(GeneralConstants.OWNER_SUBJECT);
		aceowner.setBrowsable(true);
		aceowner.setPrivileges("[\"write\",\"read\",\"delete\"]");
		aceowner.setPermissions("[\"print\",\"browse\",\"copy\"]");
		acelist.add(aceowner);
		
		CabAceInfo aceowner1 = new CabAceInfo();
		aceowner1.setSubjectType(AceType.GROUP.value);
		aceowner1.setSubject("demogrp");
		aceowner1.setBrowsable(true);
		aceowner1.setPrivileges("[\"write\",\"read\",\"delete\"]");
		aceowner1.setPermissions("[\"print\",\"browse\",\"copy\"]");
		acelist.add(aceowner1);
		
		aceowner1 = new CabAceInfo();
		aceowner1.setSubjectType(AceType.USER.value);
		aceowner1.setSubject("demousr");
		aceowner1.setBrowsable(true);
		aceowner1.setPrivileges("[\"write\",\"read\",\"delete\"]");
		aceowner1.setPermissions("[\"print\",\"browse\",\"copy\"]");
		acelist.add(aceowner1);
		
		aclService.addAclInfo(svcctx, acelist);

	}
	
	@Test
	public void test1() throws Exception{
		
		Acl acl = new Acl();
		
		Ace ace = new Ace(AceType.OWNER, "dev1", AcePrivilege.BROWSE);
		ace.grantPrivileges(AcePrivilege.READ, AcePrivilege.DELETE);
		ace.grantPermission("download", "upload", "share");
		
		acl.addAce(ace, false);
		
		ace = new Ace(AceType.ANYONE, "dev2", AcePrivilege.BROWSE);
		ace.grantPrivileges(AcePrivilege.READ, AcePrivilege.DELETE, AcePrivilege.EXEC);
		ace.grantPermission("download", "share");
		acl.addAce(ace, false);
		
		ace = new Ace(AceType.GROUP, "grp1", AcePrivilege.BROWSE);
		ace.grantPrivileges(AcePrivilege.READ, AcePrivilege.DELETE, AcePrivilege.EXEC);
		ace.grantPermission("download", "share");
		acl.addAce(ace, false);
		
		svcctx = new ServiceContext(principal);
		InfoId<Long> aclid = aclService.addAcl(svcctx, acl);

	}
	
	@Test
	public void test2() throws Exception{
		svcctx = new ServiceContext(principal);
		InfoId<Long> ckey = IdKey.CABINET.getInfoId(38l);
		Acl acl = aclService.getAcl(svcctx, ckey);
		System.out.println(acl);
	}
}
