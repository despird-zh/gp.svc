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
import com.gp.common.Users;
import com.gp.dao.GroupUserDAO;
import com.gp.info.GroupUserInfo;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class GroupUserDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = Users.PESUOD_USER;
	ServiceContext svcctx ;
	@Autowired
    private GroupUserDAO groupdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		

		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.GROUP_USER,Long.class);
		
		GroupUserInfo info = new GroupUserInfo();
		info.setInfoId(id);
		
		info.setAccount("accout1");
		info.setWorkgroupId(123l);
		info.setGroupId(234l);		
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		groupdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		int c = groupdao.update( info);
		System.out.println("--- update done:"+c);
		
		GroupUserInfo info2= groupdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = groupdao.delete( id);
		System.out.println("--- delete done:"+d);
	}
	
	@Test
	public void test1() throws Exception{
		

		svcctx = new ServiceContext(principal);
		principal.setAccount("acc001");
		for(int i = 0;i<2;i++){
			InfoId<Long> id = idService.generateId( IdKey.GROUP_USER,Long.class);
			
			GroupUserInfo info = new GroupUserInfo();
			info.setInfoId(id);
			info.setWorkgroupId(123l);
			info.setAccount("accout1");
			info.setGroupId(234l);		
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			groupdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
