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
import com.gp.common.FlatColumns.FilterMode;
import com.gp.dao.TaskDAO;
import com.gp.info.InfoId;
import com.gp.info.TaskInfo;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class TaskDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = new Principal("demouser");
	ServiceContext svcctx ;
	@Autowired
    private TaskDAO orgdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{

		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.TASK,Long.class);
			
		TaskInfo info = new TaskInfo();
		info.setInfoId(id);
		info.setSourceId(234);
		info.setWorkgroupId(222l);
		info.setTaskChronicalId(555l);
		info.setTaskName("sdfadf");
		info.setDescription("des00");
		info.setWeight(5.4);
		info.setState("sss");
		info.setDueDate(new Date(System.currentTimeMillis()));
		info.setOpinion("sss");
		info.setExecuteDate(new Date(System.currentTimeMillis()));
		info.setCompleteDate(new Date(System.currentTimeMillis()));
		info.setAsignee("sss");
		info.setOwner("ow001");
		info.setExecutor("e001");
		info.setHashCode("asfdasdf");
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		orgdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		int c = orgdao.update( info,FilterMode.NONE);
		System.out.println("--- update done:"+c);
		
		TaskInfo info2= orgdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = orgdao.delete( id);
		System.out.println("--- delete done:"+d);
	}
	
	@Test
	public void test1() throws Exception{
		
		svcctx = new ServiceContext(principal);
		principal.setAccount("acc001");
		for(int i = 0;i<5;i++){
			InfoId<Long> id = idService.generateId( IdKey.TASK,Long.class);
			
			TaskInfo info = new TaskInfo();
			info.setInfoId(id);
			info.setSourceId(234);
			
			info.setWorkgroupId(222l);
			info.setTaskChronicalId(555l);
			info.setTaskName("sdfadf");
			info.setDescription("des00");
			info.setWeight(5.4);
			info.setState("sss");
			info.setDueDate(new Date(System.currentTimeMillis()));
			info.setOpinion("sss");
			info.setExecuteDate(new Date(System.currentTimeMillis()));
			info.setCompleteDate(new Date(System.currentTimeMillis()));
			info.setAsignee("sss");
			info.setOwner("ow001");
			info.setExecutor("e001");
			info.setHashCode("sfeerfgfg");
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			orgdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
