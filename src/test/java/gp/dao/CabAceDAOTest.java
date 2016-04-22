package gp.dao;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.common.Users;
import com.gp.dao.CabAceDAO;
import com.gp.info.CabAceInfo;
import com.gp.info.InfoId;
import com.gp.svc.IdService;

@ContextConfiguration(locations = "/mysql-test.xml")
public class CabAceDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = Users.PESUOD_USER;
	ServiceContext svcctx ;
	@Autowired
    private CabAceDAO acedao;
	
	@Autowired
    private IdService idService;
	
	@Test
	public void test() throws Exception{
		

		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.CAB_ACE, Long.class);
	
		CabAceInfo info = new CabAceInfo();
		info.setInfoId(id);

		info.setAclId(123l);
		info.setSubject("user1");
		info.setSubjectType("type1");
		info.setPrivilege(6);
		info.setPermissions("[\"sss\",\"sss\",\"sss\"]");
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		acedao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		info.setSubject("sub0001");
		int c = acedao.update(  info);
		System.out.println("--- update done:"+c);
		
		CabAceInfo info2= acedao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = acedao.delete( id);
		System.out.println("--- delete done:"+d);
	}

}
