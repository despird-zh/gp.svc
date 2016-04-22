package gp.dao;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.common.Users;
import com.gp.dao.CabCommentDAO;
import com.gp.info.CabCommentInfo;
import com.gp.info.InfoId;
import com.gp.svc.IdService;

@ContextConfiguration(locations = "/mysql-test.xml")
public class CabCommentDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = Users.PESUOD_USER;
	ServiceContext svcctx ;
	@Autowired
    private CabCommentDAO commentdao;
	
	@Autowired
    private IdService idService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		principal.setAccount("testuser");
		InfoId<Long> id = idService.generateId( IdKey.CAB_COMMENT, Long.class);

		CabCommentInfo info = new CabCommentInfo();
		info.setInfoId(id);


		info.setWorkgroupId(123l);
		info.setParentId(123l);
		info.setDocId(345l);
		info.setAuthor("author01");
		info.setOwner("owner01");
		info.setContent("content setting.. ");
		info.setState("state01");
		info.setCommentDate(new Date(System.currentTimeMillis()));
		info.setHashCode("xxxx");
		info.setOwm(123l);
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		commentdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		info.setContent("sub0001");
		int c = commentdao.update( info);
		System.out.println("--- update done:"+c);
		
		CabCommentInfo info2= commentdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = commentdao.delete( id);
		System.out.println("--- delete done:"+d);
	}

}
