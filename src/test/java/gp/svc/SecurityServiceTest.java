package gp.svc;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.config.TestConfig;
import com.gp.common.IdKey;
import com.gp.common.GPrincipal;
import com.gp.common.ServiceContext;
import com.gp.info.InfoId;
import com.gp.dao.info.UserInfo;
import com.gp.svc.CommonService;
import com.gp.svc.SecurityService;

@ContextConfiguration(classes={TestConfig.class})
public class SecurityServiceTest extends AbstractJUnit4SpringContextTests{
	
	GPrincipal principal = new GPrincipal("demouser");
	ServiceContext svcctx ;
	
	@Autowired
    private CommonService idService;
	
	@Autowired
    private SecurityService securityService;
	
	@Test
	public void test() throws Exception{

		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.GP_USERS ,Long.class);
		
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
		
		securityService.newAccount(svcctx, info,120l,34l);
	}
}
