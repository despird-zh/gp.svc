package gp.dao;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.config.TestConfig;
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.common.GroupUsers;
import com.gp.dao.AuditDAO;
import com.gp.info.AuditInfo;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class AuditDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = GroupUsers.PESUOD_USER;
	ServiceContext svcctx ;
	@Autowired
    private AuditDAO auditdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		

		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.AUDIT, Long.class);
	
		AuditInfo info = new AuditInfo();
		info.setInfoId(id);
		
		info.setWorkgroupId(123l);

		info.setClient("cli001");
		info.setHost("192.168.1.1");
		info.setApp("web");
		info.setVersion("0.1");
		
		info.setVerb("add audit");
		info.setSubject("demosub user");
		info.setPredicates("{\"a\":\"bv\"}");
		info.setTarget("target");
		info.setState("complete");
		info.setMessage("message demo bla...");
		info.setAuditDate(new Date(System.currentTimeMillis()));
		info.setElapseTime(234l);
		
		info.setModifier("modifier11");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		auditdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		info.setSubject("another name");
		int c = auditdao.update(info);
		System.out.println("--- update done:"+c);
		
		AuditInfo info2= auditdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = auditdao.delete( id);
		System.out.println("--- update done:"+d);
	}

}
