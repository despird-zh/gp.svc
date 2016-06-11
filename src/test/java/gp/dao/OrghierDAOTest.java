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
import com.gp.dao.OrgHierDAO;
import com.gp.info.InfoId;
import com.gp.info.OrgHierInfo;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class OrghierDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = GroupUsers.PSEUDO_USER;
	ServiceContext svcctx ;
	@Autowired
    private OrgHierDAO orgdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.ORG_HIER,Long.class);
			
		OrgHierInfo info = new OrgHierInfo();
		info.setInfoId(id);
		
		info.setMemberGroupId(123l);
		info.setLevel("level");
		info.setParentOrg(989l);
		info.setOrgName("orgname 001");
		info.setDescription("descri0010101");
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		orgdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		info.setDescription("xxxx");
		int c = orgdao.update(  info);
		System.out.println("--- update done:"+c);
		
		OrgHierInfo info2= orgdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = orgdao.delete( id);
		System.out.println("--- delete done:"+d);
	}
	
	@Test
	public void test1() throws Exception{
		
		svcctx = new ServiceContext(principal);
		principal.setAccount("acc001");
		for(int i = 0;i<2;i++){
			InfoId<Long> id = idService.generateId(IdKey.ORG_HIER,Long.class);
			OrgHierInfo info = new OrgHierInfo();
			info.setInfoId(id);
			
			info.setMemberGroupId(123l);
			info.setLevel("level");
			info.setParentOrg(989l);
			info.setOrgName("orgname 001");
			info.setDescription("descri0010101");
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			orgdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
