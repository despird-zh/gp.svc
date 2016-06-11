package gp.dao;

import static org.junit.Assert.*;

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
import com.gp.dao.AttachDAO;
import com.gp.info.AttachInfo;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class AttachDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = GroupUsers.PSEUDO_USER;
	
	ServiceContext svcctx ;
	@Autowired
    private AttachDAO attachdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId(IdKey.ATTACHMENT, Long.class);
		
		AttachInfo info = new AttachInfo();
		info.setInfoId(id);
		info.setAttachName("name");
		info.setCreateDate(new Date(System.currentTimeMillis()));
		info.setCreator("creator");

		info.setFormat("fmt");
		info.setModifier("mdfer");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		info.setOwner("owner001");
		info.setSize(1234l);
		info.setState("uploading");
		info.setBinaryId(123456l);
		info.setWorkgroupId(123l);
		
		attachdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		info.setAttachName("another name");
		int c = attachdao.update(info);
		System.out.println("--- update done:"+c);
		
		AttachInfo info2= attachdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = attachdao.delete( id);
		System.out.println("--- delete done:"+d);
	}

}
