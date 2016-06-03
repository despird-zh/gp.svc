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
import com.gp.dao.UserDAO;
import com.gp.info.InfoId;
import com.gp.info.UserInfo;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class UserDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = new Principal("demouser");
	ServiceContext svcctx ;
	@Autowired
    private UserDAO userdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{

		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.USER,Long.class);

		UserInfo info = new UserInfo();
		info.setInfoId(id);
		
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
		info.setGlobalAccount("gggg accnt");
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		userdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		int c = userdao.update( info);
		System.out.println("--- update done:"+c);
		
		UserInfo info2= userdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = userdao.delete( id);
		System.out.println("--- delete done:"+d);
	}
	
	@Test
	public void test1() throws Exception{
		
		svcctx = new ServiceContext(principal);
		principal.setAccount("acc001");
		for(int i = 0;i<5;i++){
			
			InfoId<Long> id = idService.generateId(IdKey.USER,Long.class);
	
			UserInfo info = new UserInfo();
			info.setInfoId(id);

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
			info.setGlobalAccount("gggg accnt");
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			userdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
