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
import com.gp.common.GroupUsers;
import com.gp.dao.ShareDAO;
import com.gp.info.InfoId;
import com.gp.dao.info.ShareInfo;
import com.gp.svc.CommonService;
@ContextConfiguration(classes={TestConfig.class})
public class ShareDAOTest extends AbstractJUnit4SpringContextTests{

	GPrincipal principal = GroupUsers.PSEUDO_USER;
	ServiceContext svcctx ;
	@Autowired
    private ShareDAO orgdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id =idService.generateId( IdKey.GP_SHARES,Long.class);

		ShareInfo info = new ShareInfo();
		info.setInfoId(id);
		
		info.setWorkgroupId(123l);
		info.setSharer("share001");
		info.setTarget("sss");
		info.setOwm(333l);
		info.setShareKey("ssdf");
		info.setShareDate(new Date(System.currentTimeMillis()));
		info.setExpireDate(new Date(System.currentTimeMillis()));
		info.setAccessLimit(1);
		info.setAccessTimes(1);		
		info.setShareName("demoshare");
		info.setDescription("descr001");
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		orgdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		int c = orgdao.update(  info,FilterMode.NONE);
		System.out.println("--- update done:"+c);
		
		ShareInfo info2= orgdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = orgdao.delete( id);
		System.out.println("--- update done:"+d);
	}
	
	@Test
	public void test1() throws Exception{
		
		svcctx = new ServiceContext(principal);
		principal.setAccount("acc001");
		for(int i = 0;i<5;i++){
			InfoId<Long> id = idService.generateId( IdKey.GP_SHARES,Long.class);
		
			ShareInfo info = new ShareInfo();
			info.setInfoId(id);

			info.setShareName("demoshare");
			info.setDescription("descr001");
			info.setWorkgroupId(123l);
			info.setSharer("share001");
			info.setTarget("sss");
			info.setOwm(333l);
			info.setShareKey("ssdf");
			info.setShareDate(new Date(System.currentTimeMillis()));
			info.setExpireDate(new Date(System.currentTimeMillis()));
			info.setAccessLimit(1);
			info.setAccessTimes(1);		
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			orgdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
