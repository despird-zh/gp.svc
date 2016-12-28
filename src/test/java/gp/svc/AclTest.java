package gp.svc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.config.TestConfig;
import com.gp.acl.AcePrivilege;
import com.gp.acl.AceType;
import com.gp.common.GeneralConstants;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.common.GroupUsers;
import com.gp.dao.info.CabAceInfo;
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
		aceowner.setPrivilege(AcePrivilege.DELETE.value | AcePrivilege.EXEC.value);
		aceowner.setPermissions("[\"print\",\"browse\",\"copy\"]");
		acelist.add(aceowner);
		
		CabAceInfo aceowner1 = new CabAceInfo();
		aceowner1.setSubjectType(AceType.GROUP.value);
		aceowner1.setSubject("demogrp");
		aceowner1.setPrivilege(AcePrivilege.DELETE.value | AcePrivilege.EXEC.value);
		aceowner1.setPermissions("[\"print\",\"browse\",\"copy\"]");
		acelist.add(aceowner1);
		
		aceowner1 = new CabAceInfo();
		aceowner1.setSubjectType(AceType.USER.value);
		aceowner1.setSubject("demousr");
		aceowner1.setPrivilege(AcePrivilege.DELETE.value | AcePrivilege.EXEC.value);
		aceowner1.setPermissions("[\"print\",\"browse\",\"copy\"]");
		acelist.add(aceowner1);
		
		aclService.addAclInfo(svcctx, acelist);
	}
}
