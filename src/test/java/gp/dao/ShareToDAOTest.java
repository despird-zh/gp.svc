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
import com.gp.dao.ShareToDAO;
import com.gp.info.InfoId;
import com.gp.dao.info.ShareToInfo;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class ShareToDAOTest extends AbstractJUnit4SpringContextTests{

	GPrincipal principal = GroupUsers.PSEUDO_USER;
	ServiceContext svcctx ;
	@Autowired
    private ShareToDAO sharetodao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id =idService.generateId( IdKey.GP_SHARE_TO,Long.class);

		ShareToInfo info = new ShareToInfo();
		info.setInfoId(id);
		
		info.setWorkgroupId(123l);
		info.setAccessCount(12);
		info.setOwm(233l);
		info.setShareId(123l);
		info.setShareMode("token");
		info.setShareName("名称");
		info.setShareToken("adfakdjf");
		info.setToAccount("aaa");
		info.setToGlobalAccount("ggggg");
		info.setToEmail("wer@m.com");
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		sharetodao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		int c = sharetodao.update( info,FilterMode.NONE);
		System.out.println("--- update done:"+c);
		
		ShareToInfo info2= sharetodao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = sharetodao.delete( id);
		System.out.println("--- delete done:"+d);
	}
	
	@Test
	public void test1() throws Exception{
		
		svcctx = new ServiceContext(principal);
		principal.setAccount("acc001");
		for(int i = 0;i<5;i++){
			InfoId<Long> id = idService.generateId( IdKey.GP_SHARE_TO,Long.class);
		
			ShareToInfo info = new ShareToInfo();
			info.setInfoId(id);
			
			info.setWorkgroupId(123l);
			info.setAccessCount(12);
			info.setOwm(233l);
			info.setShareId(123l);
			info.setShareMode("token");
			info.setShareName("名称");
			info.setShareToken("adfakdjf");
			info.setToAccount("aaa");
			info.setToGlobalAccount("ggggg");
			info.setToEmail("wer@m.com");
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			sharetodao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
