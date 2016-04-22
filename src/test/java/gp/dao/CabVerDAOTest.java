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
import com.gp.dao.CabVersionDAO;
import com.gp.info.CabVersionInfo;
import com.gp.info.InfoId;
import com.gp.svc.IdService;

@ContextConfiguration(locations = "/mysql-test.xml")
public class CabVerDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = Users.PESUOD_USER;
	ServiceContext svcctx ;
	@Autowired
    private CabVersionDAO filedao;
	
	@Autowired
    private IdService idService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.CAB_VERSION,Long.class);
		
		CabVersionInfo info = new CabVersionInfo();
		info.setInfoId(id);

		info.setCabinetId(123l);
		info.setParentId(123l);
		info.setFileId(345l);
		info.setFileName("folderna");
		info.setDescription("descr...");
		info.setProfile("sssss");
		info.setProperties("{sssss}");
		info.setVersionLabel("demo version");
		info.setSize(345l);
		info.setOwner("owner");
		info.setCommentOn(true);
		info.setVersion("v0.1");
		info.setState("lock");
		info.setBinaryId(687l);
		info.setFormat("docx");
		info.setCreator("cr001");
		info.setCreateDate(new Date(System.currentTimeMillis()));
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		filedao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		info.setDescription("sub0001");
		int c = filedao.update(info);
		System.out.println("--- update done:"+c);
		
		CabVersionInfo info2= filedao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = filedao.delete( id);
		System.out.println("--- delete done:"+d);
	}
	
	@Test
	public void test1() throws Exception{

		svcctx = new ServiceContext(principal);
		principal.setAccount("accou01");
		for(int i = 0;i<5;i++){
			InfoId<Long> id = idService.generateId( IdKey.CAB_VERSION,Long.class);
			
			CabVersionInfo info = new CabVersionInfo();
			info.setInfoId(id);
			

			info.setCabinetId(123l);
			info.setParentId(123l);
			info.setFileId(345l);
			info.setFileName("folderna");
			info.setDescription("descr...");
			info.setProfile("sssss");
			info.setProperties("{sssss}");
			info.setVersionLabel("demo version");
			info.setSize(345l);
			info.setOwner("owner");
			info.setCommentOn(true);
			info.setVersion("v0.1");
			info.setState("lock");
			info.setBinaryId(687l);
			info.setFormat("docx");
			info.setCreator("cr001");
			info.setCreateDate(new Date(System.currentTimeMillis()));
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			filedao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
