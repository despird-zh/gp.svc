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
import com.gp.dao.PostCommentDAO;
import com.gp.info.InfoId;
import com.gp.info.PostCommentInfo;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class PostCommentDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = GroupUsers.PESUOD_USER;
	ServiceContext svcctx ;
	@Autowired
    private PostCommentDAO commentdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		principal.setAccount("testuser");
		InfoId<Long> id = idService.generateId( IdKey.POST_COMMENT,Long.class);
		
		PostCommentInfo info = new PostCommentInfo();
		info.setInfoId(id);
		
		info.setWorkgroupId(123l);
		info.setParentId(123l);
		info.setPostId(345l);
		info.setHashCode("rsc_type");
		info.setAuthor("author01");
		info.setOwner("owner01");
		info.setContent("content setting.. ");
		info.setState("state01");
		info.setCommentDate(new Date(System.currentTimeMillis()));
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		commentdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		info.setContent("sub0001");
		int c = commentdao.update(  info);
		System.out.println("--- update done:"+c);
		
		PostCommentInfo info2= commentdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = commentdao.delete( id);
		System.out.println("--- delete done:"+d);
	}

}
