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
import com.gp.dao.GroupDAO;
import com.gp.info.GroupInfo;
import com.gp.info.InfoId;
import com.gp.svc.IdService;

@ContextConfiguration(locations = "/mysql-test.xml")
public class GroupDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = Users.PESUOD_USER;
	ServiceContext svcctx ;
	@Autowired
    private GroupDAO groupdao;
	
	@Autowired
    private IdService idService;
	
	@Test
	public void test() throws Exception{
		

		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.GROUP,Long.class);
	
		GroupInfo info = new GroupInfo();
		info.setInfoId(id);
			
		info.setWorkgroupId(123l);
		info.setGroupName("g name");
		info.setDescription("descr");
		
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		groupdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		info.setDescription("sub00011");
		int c = groupdao.update( info);
		System.out.println("--- update done:"+c);
		
		GroupInfo info2= groupdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = groupdao.delete( id);
		System.out.println("--- delete done:"+d);
	}
	
	@Test
	public void test1() throws Exception{
		
		svcctx = new ServiceContext(principal);
		principal.setAccount("acc001");
		for(int i = 0;i<2;i++){
			InfoId<Long> id = idService.generateId( IdKey.GROUP,Long.class);
		
			GroupInfo info = new GroupInfo();
			info.setInfoId(id);
						
			info.setWorkgroupId(123l);
			info.setGroupName("g name");
			info.setDescription("descr");
						
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			groupdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
