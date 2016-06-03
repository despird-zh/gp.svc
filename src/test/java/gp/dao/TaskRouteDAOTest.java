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
import com.gp.dao.TaskDAO;
import com.gp.dao.TaskRouteDAO;
import com.gp.info.InfoId;
import com.gp.info.TaskInfo;
import com.gp.info.TaskRouteInfo;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class TaskRouteDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = Users.PESUOD_USER;
	ServiceContext svcctx ;
	@Autowired
    private TaskRouteDAO orgdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.TASK_ROUTE,Long.class);
		
		TaskRouteInfo info = new TaskRouteInfo();
		info.setInfoId(id);
		
		info.setWorkgroupId(123l);
		info.setChronicalTaskId(444l);
		info.setTaskId(555l);
		info.setForwardTaskId(444l);
		info.setExecutor("exe001");
		info.setOwner("ooo001");
		info.setState("sss001");
		info.setFordwardDate(new Date(System.currentTimeMillis()));
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		orgdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		int c = orgdao.update(  info);
		System.out.println("--- update done:"+c);
		
		TaskRouteInfo info2= orgdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = orgdao.delete( id);
		System.out.println("--- update done:"+d);
	}
	
	@Test
	public void test1() throws Exception{
		
		svcctx = new ServiceContext(principal);
		principal.setAccount("acc001");
		for(int i = 0;i<5;i++){
			InfoId<Long> id = idService.generateId( IdKey.TASK_ROUTE,Long.class);
			
			TaskRouteInfo info = new TaskRouteInfo();
			info.setInfoId(id);			
			info.setWorkgroupId(123l);
			info.setChronicalTaskId(444l);
			info.setTaskId(555l);
			info.setForwardTaskId(444l);
			info.setExecutor("exe001");
			info.setOwner("ooo001");
			info.setState("sss001");
			info.setFordwardDate(new Date(System.currentTimeMillis()));
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			orgdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
