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
import com.gp.dao.ChatMessageDispatchDAO;
import com.gp.info.InfoId;
import com.gp.dao.info.ChatMessageDispatchInfo;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class MessageDispatchDAOTest extends AbstractJUnit4SpringContextTests{

	GPrincipal principal = GroupUsers.PSEUDO_USER;
	ServiceContext svcctx ;
	@Autowired
    private ChatMessageDispatchDAO msgdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.MESSAGE_DISPATCH,Long.class);
			
		ChatMessageDispatchInfo info = new ChatMessageDispatchInfo();
		info.setInfoId(id);
		
		info.setMessageId(123l);
		info.setTouchFlag(false);
		info.setTouchTime(new Date(System.currentTimeMillis()));
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		msgdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		int c = msgdao.update( info,FilterMode.NONE);
		System.out.println("--- update done:"+c);
		
		ChatMessageDispatchInfo info2= msgdao.query( id);
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
			
			ChatMessageDispatchInfo info = new ChatMessageDispatchInfo();
			info.setInfoId(id);
			
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
