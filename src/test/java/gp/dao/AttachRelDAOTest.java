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
import com.gp.dao.AttachRelDAO;
import com.gp.dao.info.AttachRelInfo;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class AttachRelDAOTest extends AbstractJUnit4SpringContextTests{

	GPrincipal principal = GroupUsers.PSEUDO_USER;
	ServiceContext svcctx ;
	@Autowired
    private AttachRelDAO attachreldao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{

		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.GP_ATTACH_REL, Long.class);
		
		AttachRelInfo info = new AttachRelInfo();
		info.setInfoId(id);
		
		info.setWorkgroupId(123l);
		info.setAttachId(234l);
		info.setResourceId(345l);
		info.setResourceType("doc");
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		attachreldao.create(info);
		System.out.println("--- create done:"+id.toString());
		
		info.setWorkgroupId(345678l);;
		int c = attachreldao.update(  info,FilterMode.NONE);
		System.out.println("--- update done:"+c);
		
		AttachRelInfo info2= attachreldao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = attachreldao.delete( id);
		System.out.println("--- delete done:"+d);
	}

}
