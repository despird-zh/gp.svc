package gp.dao;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.config.TestConfig;
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.common.GroupUsers;
import com.gp.dao.SourceDAO;
import com.gp.info.SourceInfo;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class InstanceDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = GroupUsers.PSEUDO_USER;
	ServiceContext svcctx ;
	@Autowired
    private SourceDAO entitydao;
	
	@Autowired
    private CommonService idService;
	
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Integer> id = IdKey.SOURCE.getInfoId(-9999);
		
		SourceInfo info = new SourceInfo();
		info.setInfoId(id);
		
		info.setEntityCode("E00001");
		info.setNodeCode("N00002");
		info.setSourceName("demo entity");
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
		
		SourceInfo info2= entitydao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = entitydao.delete( id);
		System.out.println("--- update done:"+d);
	}
	
	@Test
	public void test1() throws Exception{
		
		principal.setAccount("acc001");
		for(int i = 0;i<20;i++){
			InfoId<Integer> id = idService.generateId(IdKey.SOURCE, Integer.class);
			
			SourceInfo info = new SourceInfo();
			info.setInfoId(id);
			info.setEntityCode("E00001");
			info.setNodeCode("N0000"+(i+3));
			info.setSourceName("demo entity"+i);
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
