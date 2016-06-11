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
import com.gp.common.GroupUsers;
import com.gp.dao.PostDAO;
import com.gp.info.InfoId;
import com.gp.info.PostInfo;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class PostDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = GroupUsers.PSEUDO_USER;
	ServiceContext svcctx ;
	@Autowired
    private PostDAO orgdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		

		svcctx = new ServiceContext(principal);
		InfoId<Long> id = idService.generateId( IdKey.POST,Long.class);
			
		PostInfo info = new PostInfo();
		info.setInfoId(id);
		
		info.setWorkgroupId(120l);
		info.setOwner("own001");
		info.setContent("content -- -");
		info.setExcerpt("exc---");
		info.setTitle("this is title");
		info.setState("state");
		info.setCommentOn(true);
		info.setPostType("posttype");
		info.setCommentCount(3);
		info.setUpvoteCount(3);
		info.setDownvoteCount(4);
		info.setPostDate(new Date(System.currentTimeMillis()));
		info.setHashCode("hxxxsos");
		info.setOwm(123l);
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		orgdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		int c = orgdao.update( info);
		System.out.println("--- update done:"+c);
		
		PostInfo info2= orgdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = orgdao.delete( id);
		System.out.println("--- delete done:"+d);
	}
	
	@Test
	public void test1() throws Exception{
		
		svcctx = new ServiceContext(principal);
		principal.setAccount("acc001");
		for(int i = 0;i<5;i++){
			InfoId<Long> id = idService.generateId( IdKey.POST,Long.class);
				
			PostInfo info = new PostInfo();
			info.setInfoId(id);
						
			info.setWorkgroupId(120l);
			info.setOwner("own001");
			info.setContent("content -- -");
			info.setExcerpt("exc---");
			info.setTitle("this is title");
			info.setState("state");
			info.setCommentOn(true);
			info.setPostType("posttype");
			info.setCommentCount(3);
			info.setUpvoteCount(3);
			info.setDownvoteCount(4);
			info.setPostDate(new Date(System.currentTimeMillis()));
			info.setHashCode("hxxxsos");
			info.setOwm(123l);
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			orgdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
