package gp.svc;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.info.InfoId;
import com.gp.info.UserInfo;
import com.gp.svc.IdService;
import com.gp.svc.SecurityService;

@ContextConfiguration(locations = "/mysql-test.xml")
public class SecurityServiceTest extends AbstractJUnit4SpringContextTests{
	
	Principal principal = new Principal("demouser");
	ServiceContext svcctx ;
	
	@Autowired
    private IdService idService;
	
	@Autowired
    private SecurityService securityService;
	
	@Test
	public void test() throws Exception{

		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.USER ,Long.class);
		
		UserInfo info = new UserInfo();
		info.setInfoId(id);
		info.setSourceId(341243);
		info.setAccount("u001");
		info.setType("ty01");
		info.setMobile("12398475");
		info.setPhone("29384874");
		info.setFullName("fname0");
		info.setEmail("22@s.com");
		info.setPassword("ssss");
		info.setState("ss");
		info.setCreateDate(new Date(System.currentTimeMillis()));
		info.setExtraInfo("sss{}");
		info.setRetryTimes(4);
		info.setLastLogonDate(new Date(System.currentTimeMillis()));
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		securityService.newAccount(svcctx, info);
	}
}
