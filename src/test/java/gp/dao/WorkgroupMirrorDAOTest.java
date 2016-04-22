package gp.dao;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.dao.TaskDAO;
import com.gp.dao.TaskRouteDAO;
import com.gp.dao.VoteDAO;
import com.gp.dao.WorkgroupDAO;
import com.gp.dao.WorkgroupMirrorDAO;
import com.gp.info.InfoId;
import com.gp.info.TaskInfo;
import com.gp.info.TaskRouteInfo;
import com.gp.info.VoteInfo;
import com.gp.info.WorkgroupInfo;
import com.gp.info.WorkgroupMirrorInfo;
import com.gp.svc.IdService;

@ContextConfiguration(locations = "/mysql-test.xml")
public class WorkgroupMirrorDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = new Principal("demouser");
	ServiceContext svcctx ;
	@Autowired
    private WorkgroupMirrorDAO orgdao;
	
	@Autowired
    private IdService idService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.WORKGROUP_MIRROR,Long.class);
		
		WorkgroupMirrorInfo info = new WorkgroupMirrorInfo();
		info.setInfoId(id);
		
		info.setLastSyncDate(new Date(System.currentTimeMillis()));
		info.setWorkgroupId(123l);
		info.setOwm(123l);
		info.setState("sta");
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		orgdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		int c = orgdao.update( info);
		System.out.println("--- update done:"+c);
		
		WorkgroupMirrorInfo info2= orgdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = orgdao.delete( id);
		System.out.println("--- delete done:"+d);
	}
	
	@Test
	public void test1() throws Exception{
		
		svcctx = new ServiceContext(principal);
		principal.setAccount("acc001");
		for(int i = 0;i<5;i++){
			InfoId<Long> id = idService.generateId( IdKey.WORKGROUP_MIRROR,Long.class);
		
			WorkgroupMirrorInfo info = new WorkgroupMirrorInfo();
			info.setInfoId(id);
						
			info.setLastSyncDate(new Date(System.currentTimeMillis()));
			info.setWorkgroupId(123l);
			info.setOwm(123l);
			info.setState("sta");
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			orgdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
