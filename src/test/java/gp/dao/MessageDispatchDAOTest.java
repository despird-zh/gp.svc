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
import com.gp.dao.MessageDAO;
import com.gp.dao.MessageDispatchDAO;
import com.gp.info.InfoId;
import com.gp.info.MessageDispatchInfo;
import com.gp.info.MessageInfo;
import com.gp.svc.IdService;

@ContextConfiguration(locations = "/mysql-test.xml")
public class MessageDispatchDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = Users.PESUOD_USER;
	ServiceContext svcctx ;
	@Autowired
    private MessageDispatchDAO msgdao;
	
	@Autowired
    private IdService idService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.MESSAGE_DISPATCH,Long.class);
			
		MessageDispatchInfo info = new MessageDispatchInfo();
		info.setInfoId(id);
		
		info.setAccount("acc");
		info.setGlobalAccount("gacc");
		info.setMessageContent("xxxxx");
		info.setMessageId(123l);
		info.setTouchFlag(false);
		info.setTouchTime(new Date(System.currentTimeMillis()));
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		msgdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		int c = msgdao.update( info);
		System.out.println("--- update done:"+c);
		
		MessageDispatchInfo info2= msgdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = msgdao.delete( id);
		System.out.println("--- delete done:"+d);
	}
	
	@Test
	public void test1() throws Exception{
		
		svcctx = new ServiceContext(principal);
		principal.setAccount("acc001");
		for(int i = 0;i<5;i++){
			InfoId<Long> id = idService.generateId( IdKey.MESSAGE_DISPATCH,Long.class);
			
			MessageDispatchInfo info = new MessageDispatchInfo();
			info.setInfoId(id);
			
			info.setAccount("acc" + i);
			info.setGlobalAccount("gacc" +i);
			info.setMessageContent("xxxxx" + i);
			info.setMessageId(123l);
			info.setTouchFlag(false);
			info.setTouchTime(new Date(System.currentTimeMillis()));
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			msgdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
