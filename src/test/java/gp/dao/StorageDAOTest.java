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
import com.gp.dao.ShareDAO;
import com.gp.dao.ShareToDAO;
import com.gp.dao.StorageDAO;
import com.gp.info.InfoId;
import com.gp.info.ShareInfo;
import com.gp.info.ShareToInfo;
import com.gp.info.StorageInfo;
import com.gp.svc.CommonService;

@ContextConfiguration(locations = "/mysql-test.xml")
public class StorageDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = Users.PESUOD_USER;
	ServiceContext svcctx ;
	@Autowired
    private StorageDAO storagedao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Integer> id =idService.generateId( IdKey.STORAGE,Integer.class);

		StorageInfo info = new StorageInfo();
		info.setInfoId(id);
		
		info.setCapacity(123l);
		info.setUsed(100l);
		info.setSettingJson("{ssss:sss}");
		info.setStorageName("demo");
		info.setStorageType("DISK");
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		storagedao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		int c = storagedao.update( info);
		System.out.println("--- update done:"+c);
		
		StorageInfo info2= storagedao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = storagedao.delete( id);
		System.out.println("--- delete done:"+d);
	}
	
	@Test
	public void test1() throws Exception{
		
		svcctx = new ServiceContext(principal);
		principal.setAccount("acc001");
		for(int i = 0;i<20;i++){
			InfoId<Integer> id =idService.generateId( IdKey.STORAGE,Integer.class);

			StorageInfo info = new StorageInfo();
			info.setInfoId(id);
			
			info.setCapacity(123l);
			info.setUsed(100l);
			info.setSettingJson("{ssss:sss}");
			info.setStorageName("demo");
			info.setStorageType("DISK");
			info.setState("OPEN");
			info.setDescription("this is demo descriptin");
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			storagedao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
