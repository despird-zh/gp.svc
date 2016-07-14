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
import com.gp.common.FlatColumns.FilterMode;
import com.gp.common.GroupUsers;
import com.gp.dao.CabFolderDAO;
import com.gp.dao.info.CabFolderInfo;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class CabFolderDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = GroupUsers.PSEUDO_USER;
	ServiceContext svcctx ;
	@Autowired
    private CabFolderDAO folderdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.CAB_FOLDER,Long.class);
	
		CabFolderInfo info = new CabFolderInfo();
		info.setInfoId(id);


		info.setCabinetId(123l);
		info.setParentId(123l);
		info.setEntryName("folderna");
		info.setDescription("descr...");
		info.setProfile("sssss");
		info.setProperties("{sssss}");
		info.setAclId(123l);
		info.setTotalSize(345l);
		info.setOwner("owner");
		info.setFileCount(12);
		info.setFolderCount(13);
		info.setState("lock");
		info.setOwm(123l);
		info.setHashCode("xxxx");
		info.setCreator("cr001");
		info.setCreateDate(new Date(System.currentTimeMillis()));
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		folderdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		info.setDescription("sub0001");
		int c = folderdao.update( info,FilterMode.NONE);
		System.out.println("--- update done:"+c);
		
		CabFolderInfo info2= folderdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = folderdao.delete( id);
		System.out.println("--- delete done:"+d);
	}

	@Test
	public void test1() throws Exception{
		
		svcctx = new ServiceContext(principal);
		for(int i = 0;i<5;i++){
			InfoId<Long> id = idService.generateId( IdKey.CAB_FOLDER, Long.class);
		
			CabFolderInfo info = new CabFolderInfo();
			info.setInfoId(id);
	
			info.setCabinetId(123l);
			info.setParentId(123l);
			info.setEntryName("foler"+i);
			info.setDescription("descr 描述..."+i);
			info.setProfile("sssss");
			info.setProperties("{sssss}");
			info.setHashCode("223ddsf4tg");
			info.setAclId(123l);
			info.setTotalSize(345l);
			info.setOwner("owner");
			info.setFileCount(12);
			info.setFolderCount(13);
			info.setState("lock");
			info.setOwm(123l);
			info.setCreator("cr001");
			info.setCreateDate(new Date(System.currentTimeMillis()));
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			folderdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
