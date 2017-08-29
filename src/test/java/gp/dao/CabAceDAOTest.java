package gp.dao;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.config.TestConfig;
import com.gp.common.IdKey;
import com.gp.common.GPrincipal;
import com.gp.common.ServiceContext;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.common.GroupUsers;
import com.gp.dao.CabAceDAO;
import com.gp.dao.info.CabAceInfo;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class CabAceDAOTest extends AbstractJUnit4SpringContextTests{

	GPrincipal principal = GroupUsers.PSEUDO_USER;
	ServiceContext svcctx ;
	@Autowired
    private CabAceDAO acedao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		

		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.CAB_ACE, Long.class);
	
		CabAceInfo info = new CabAceInfo();
		info.setInfoId(id);

		info.setAclId(123l);
		info.setSubject("user1");
		info.setSubjectType("type1");
		info.setPrivileges("[\"write\",\"read\",\"delete\"]");
		info.setPermissions("[\"sss\",\"sss\",\"sss\"]");
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		acedao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		info.setSubject("sub0001");
		int c = acedao.update(  info,FilterMode.NONE);
		System.out.println("--- update done:"+c);
		
		CabAceInfo info2= acedao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = acedao.delete( id);
		System.out.println("--- delete done:"+d);
	}

}
