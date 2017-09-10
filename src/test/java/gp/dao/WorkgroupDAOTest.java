package gp.dao;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.config.TestConfig;
import com.gp.common.IdKey;
import com.gp.common.GPrincipal;
import com.gp.common.ServiceContext;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.dao.WorkgroupDAO;
import com.gp.info.InfoId;
import com.gp.dao.info.WorkgroupInfo;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class WorkgroupDAOTest extends AbstractJUnit4SpringContextTests{

	GPrincipal principal = new GPrincipal("demouser");
	ServiceContext svcctx ;
	@Autowired
    private WorkgroupDAO orgdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.GP_WORKGROUPS,Long.class);
		
		WorkgroupInfo info = new WorkgroupInfo();
		info.setInfoId(id);
		
		info.setWorkgroupName("wname");
		info.setDescription("dddd");
		info.setState("s111");
		info.setAdmin("s34");
		info.setCreator("u001");
		info.setCreateDate(new Date(System.currentTimeMillis()));
		info.setPublishCabinet(12l);
		info.setNetdiskCabinet(23l);
		
		info.setOwm(123l);
		info.setShareEnable(true);
		info.setLinkEnable(false);
		info.setNetdiskEnable(false);
		info.setPublishEnable(true);
		info.setTaskEnable(true);
		info.setPostEnable(true);
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		orgdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		int c = orgdao.update( info,FilterMode.NONE);
		System.out.println("--- update done:"+c);
		
		WorkgroupInfo info2= orgdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = orgdao.delete( id);
		System.out.println("--- delete done:"+d);
	}
	
	@Test
	public void test1() throws Exception{
		
		svcctx = new ServiceContext(principal);
		principal.setAccount("acc001");
		for(int i = 0;i<5;i++){
			InfoId<Long> id = idService.generateId( IdKey.GP_WORKGROUPS,Long.class);
		
			WorkgroupInfo info = new WorkgroupInfo();
			info.setInfoId(id);
						
			info.setPublishCabinet(12l);
			info.setNetdiskCabinet(23l);
			info.setWorkgroupName("wname");
			info.setDescription("dddd");
			info.setState("s111");
			info.setAdmin("s34");
			
			info.setTraceCode("5iqwyerkxdfhsakdjhf");
			info.setOwm(123l);
			info.setShareEnable(true);
			info.setLinkEnable(false);
			info.setNetdiskEnable(false);
			info.setPublishEnable(true);
			info.setTaskEnable(true);
			info.setPostEnable(true);
			
			info.setCreator("u001");
			info.setCreateDate(new Date(System.currentTimeMillis()));
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			orgdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
