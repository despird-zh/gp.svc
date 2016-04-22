package gp.dao;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.common.Users;
import com.gp.dao.InstanceDAO;
import com.gp.info.InstanceInfo;
import com.gp.info.InfoId;
import com.gp.svc.IdService;

@ContextConfiguration(locations = "/mysql-test.xml")
public class InstanceDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = Users.PESUOD_USER;
	ServiceContext svcctx ;
	@Autowired
    private InstanceDAO entitydao;
	
	@Autowired
    private IdService idService;
	
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Integer> id = IdKey.INSTANCE.getInfoId(-9999);
		
		InstanceInfo info = new InstanceInfo();
		info.setInfoId(id);
		
		info.setEntityCode("E00001");
		info.setNodeCode("N00002");
		info.setInstanceName("demo entity");
		info.setDescription("description bla...");
		info.setAbbr("AB01");
		info.setAdmin("admin slsls");
		info.setShortName("short name");
				
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		entitydao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		info.setDescription("sub00011");
		int c = entitydao.update( info);
		System.out.println("--- update done:"+c);
		
		InstanceInfo info2= entitydao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = entitydao.delete( id);
		System.out.println("--- update done:"+d);
	}
	
	@Test
	public void test1() throws Exception{
		
		principal.setAccount("acc001");
		for(int i = 0;i<20;i++){
			InfoId<Integer> id = idService.generateId(IdKey.INSTANCE, Integer.class);
			
			InstanceInfo info = new InstanceInfo();
			info.setInfoId(id);
			info.setEntityCode("E00001");
			info.setNodeCode("N0000"+(i+3));
			info.setInstanceName("demo entity"+i);
			info.setDescription("description bla...");
			info.setAbbr("AB01");
			info.setAdmin("admin slsls");
			info.setShortName("short name");
			info.setHashKey("sldfjdheieudjd"+i);
			info.setAdmin("admin"+i);
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			entitydao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
