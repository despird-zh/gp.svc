package gp.svc;

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
import com.gp.info.InfoId;
import com.gp.info.UserInfo;
import com.gp.info.WorkgroupInfo;
import com.gp.svc.CommonService;
import com.gp.svc.SecurityService;
import com.gp.svc.WorkgroupService;

@ContextConfiguration(classes={TestConfig.class})
public class WorkgroupServiceTest extends AbstractJUnit4SpringContextTests{
	
	Principal principal = GroupUsers.PESUOD_USER;
	ServiceContext svcctx ;
	
	@Autowired
    private CommonService idService;
	
	@Autowired
    private WorkgroupService wrokgroupService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.WORKGROUP,Long.class);
		
		WorkgroupInfo info = new WorkgroupInfo();
		info.setInfoId(id);
		info.setSourceId(987);
		info.setWorkgroupName("wname");
		info.setDescription("dddd");
		info.setState("s111");
		info.setAdmin("s34");
		info.setCreator("u001");
		info.setCreateDate(new Date(System.currentTimeMillis()));
		info.setPublishCabinet(12l);
		info.setNetdiskCabinet(23l);
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		wrokgroupService.newWorkgroup(svcctx, info, 123l, 12l);
	}
}
