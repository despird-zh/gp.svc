package gp.dao;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.config.TestConfig;
import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.common.GroupUsers;
import com.gp.dao.CabAceDAO;
import com.gp.dao.CabAclDAO;
import com.gp.info.CabAceInfo;
import com.gp.info.CabAclInfo;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class CabAclDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = GroupUsers.PESUOD_USER;
	ServiceContext svcctx ;
	@Autowired
    private CabAclDAO acldao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		

		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.CAB_ACL, Long.class);
	
		CabAclInfo info = new CabAclInfo();
		info.setInfoId(id);

		info.setAclHash("xxxx");
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		acldao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		info.setAclHash("sub0001");
		int c = acldao.update(  info);
		System.out.println("--- update done:"+c);
		
		CabAclInfo info2= acldao.query(id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = acldao.delete( id);
		System.out.println("--- delete done:"+d);
	}

}
