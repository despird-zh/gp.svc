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
import com.gp.common.GroupUsers;
import com.gp.dao.TagRelDAO;
import com.gp.info.InfoId;
import com.gp.info.TagRelInfo;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class TagRelDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = GroupUsers.PSEUDO_USER;
	ServiceContext svcctx ;
	@Autowired
    private TagRelDAO orgdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.TAG_REL,Long.class);
			
		TagRelInfo info = new TagRelInfo();
		info.setInfoId(id);

		info.setResourceId(323l);
		info.setTagName("thhh");
		info.setResourceType("ttt");
		info.setCategory("ppp");
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		orgdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		int c = orgdao.update(info,FilterMode.NONE);
		System.out.println("--- update done:"+c);
		
		TagRelInfo info2= orgdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = orgdao.delete( id);
		System.out.println("--- update done:"+d);
	}
	
	@Test
	public void test1() throws Exception{
		
		svcctx = new ServiceContext(principal);
		principal.setAccount("acc001");
		for(int i = 0;i<5;i++){
			InfoId<Long> id = idService.generateId( IdKey.TAG_REL,Long.class);
				
			TagRelInfo info = new TagRelInfo();
			info.setInfoId(id);

			info.setResourceId(323l);
			info.setTagName("thhh");
			info.setResourceType("ttt");
			info.setCategory("ppp");
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			orgdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
