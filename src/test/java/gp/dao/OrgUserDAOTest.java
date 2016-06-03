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
import com.gp.dao.OrgHierDAO;
import com.gp.dao.OrgUserDAO;
import com.gp.info.InfoId;
import com.gp.info.OrgHierInfo;
import com.gp.info.OrgUserInfo;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class OrgUserDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = Users.PESUOD_USER;
	ServiceContext svcctx ;
	@Autowired
    private OrgUserDAO orgdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.ORG_USER,Long.class);
		
		OrgUserInfo info = new OrgUserInfo();
		info.setInfoId(id);
		
		info.setOrgId(123l);
		info.setAccount("mmm");
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		orgdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		int c = orgdao.update( info);
		System.out.println("--- update done:"+c);
		
		OrgUserInfo info2= orgdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = orgdao.delete( id);
		System.out.println("--- delete done:"+d);
	}
	
	@Test
	public void test1() throws Exception{
		
		svcctx = new ServiceContext(principal);
		principal.setAccount("acc001");
		for(int i = 0;i<5;i++){
			InfoId<Long> id = idService.generateId( IdKey.ORG_USER,Long.class);
			
			OrgUserInfo info = new OrgUserInfo();
			info.setInfoId(id);
					
			
			info.setOrgId(123l);
			info.setAccount("mmm");
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			orgdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
