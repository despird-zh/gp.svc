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
import com.gp.info.InfoId;
import com.gp.info.TaskInfo;
import com.gp.info.TaskRouteInfo;
import com.gp.info.VoteInfo;
import com.gp.svc.CommonService;

@ContextConfiguration(locations = "/mysql-test.xml")
public class VoteDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = new Principal("demouser");
	ServiceContext svcctx ;
	@Autowired
    private VoteDAO orgdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.VOTE,Long.class);
	
		VoteInfo info = new VoteInfo();
		info.setInfoId(id);
		
		info.setWorkgroupId(111l);
		info.setResourceId(444l);
		info.setResourceType("r43");
		info.setVoter("us01");
		info.setOpinion("agree");
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		orgdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		int c = orgdao.update( info);
		System.out.println("--- update done:"+c);
		
		VoteInfo info2= orgdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = orgdao.delete( id);
		System.out.println("--- update done:"+d);
	}
	
	@Test
	public void test1() throws Exception{

		svcctx = new ServiceContext(principal);
		principal.setAccount("acc001");
		for(int i = 0;i<5;i++){
			InfoId<Long> id = idService.generateId( IdKey.VOTE,Long.class);
			
			VoteInfo info = new VoteInfo();
			info.setInfoId(id);			
			
			info.setWorkgroupId(111l);
			info.setResourceId(444l);
			info.setResourceType("r43");
			info.setVoter("us01");
			info.setOpinion("agree");
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			orgdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
