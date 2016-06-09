package gp.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.config.TestConfig;
import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.common.GroupUsers;
import com.gp.info.InfoId;

import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class IdServiceTest extends AbstractJUnit4SpringContextTests{
	
	Principal principal = GroupUsers.PESUOD_USER;
	ServiceContext svcctx ;
	@Autowired
    private CommonService idService;
	
	@Test
	public void testBusiestTask() throws Exception {
		
		svcctx = new ServiceContext(principal);
		
	    InfoId<Long> id= (InfoId<Long>)idService.generateId(IdKey.USER,Long.class);
	    System.out.println("----test result :" + id);
	    
	    InfoId<String> aid= (InfoId<String>)idService.generateId( IdKey.AUDIT,String.class);
	    System.out.println("----test result :" + aid);
	}

}
