package gp.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.common.Users;
import com.gp.dao.TaskDAO;
import com.gp.dao.TaskRouteDAO;
import com.gp.dao.VoteDAO;
import com.gp.dao.WorkgroupUserDAO;
import com.gp.info.InfoId;
import com.gp.info.TaskInfo;
import com.gp.info.TaskRouteInfo;
import com.gp.info.VoteInfo;
import com.gp.info.WorkgroupUserInfo;
import com.gp.svc.CommonService;

@ContextConfiguration(locations = "/mysql-test.xml")
public class WorkgroupUserDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = Users.PESUOD_USER;
	ServiceContext svcctx ;
	@Autowired
    private WorkgroupUserDAO orgdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		

		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId(IdKey.WORKGROUP_USER,Long.class);
		
		WorkgroupUserInfo info = new WorkgroupUserInfo();
		info.setInfoId(id);
		
		info.setAccount("用户");
		info.setRole("r001");
		info.setWorkgroupId(3l);
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		orgdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		int c = orgdao.update( info);
		System.out.println("--- update done:"+c);
		
		WorkgroupUserInfo info2= orgdao.query( id);
		System.out.println("--- query done:"+info2.toString()+"lm : " + info2.getModifyDate());
		
		String formatStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(info2.getModifyDate());
        System.out.println(formatStr); 
		
        int d = orgdao.delete( id);
		System.out.println("--- update done:"+d);
		
	}
	
	@Test
	public void test1() throws Exception{
		
		svcctx = new ServiceContext(principal);
		principal.setAccount("acc001");
		for(int i = 0;i<1;i++){

			InfoId<Long> id = idService.generateId(IdKey.WORKGROUP_USER,Long.class);
			
			WorkgroupUserInfo info = new WorkgroupUserInfo();
			info.setInfoId(id);			
			
			info.setGlobalAccount("gacc01");
			info.setOwm(345l);
			info.setAccount("用户");
			info.setRole("r001");
			info.setWorkgroupId(3l);
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			orgdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
