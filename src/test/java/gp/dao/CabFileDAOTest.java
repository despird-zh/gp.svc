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
import com.gp.dao.CabFileDAO;
import com.gp.dao.info.CabFileInfo;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class CabFileDAOTest extends AbstractJUnit4SpringContextTests{

	GPrincipal principal = GroupUsers.PSEUDO_USER;
	ServiceContext svcctx ;
	@Autowired
    private CabFileDAO filedao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		

		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId(IdKey.GP_CAB_FILES, Long.class);
	
		CabFileInfo info = new CabFileInfo();
		info.setInfoId(id);


		info.setCabinetId(123l);
		info.setParentId(123l);
		info.setEntryName("folderna");
		info.setDescription("descr...");
		info.setProfile("sssss");
		info.setProperties("{sssss}");
		info.setAclId(123l);
		info.setSize(345l);
		info.setOwner("owner");
		info.setCommentOn(true);
		info.setVersion("v0.1");
		info.setState("lock");
		info.setBinaryId(687l);
		info.setFormat("docx");
		info.setCreator("cr001");
		info.setHashCode("xxx");
		info.setOwm(123l);
		info.setCreateDate(new Date(System.currentTimeMillis()));
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		filedao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		info.setDescription("sub0001");
		int c = filedao.update(  info,FilterMode.NONE);
		System.out.println("--- update done:"+c);
		
		CabFileInfo info2= filedao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = filedao.delete( id);
		System.out.println("--- update done:"+d);
	}

}
