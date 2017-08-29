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
import com.gp.dao.CabinetDAO;
import com.gp.dao.info.CabinetInfo;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class CabDAOTest extends AbstractJUnit4SpringContextTests{

	GPrincipal principal = GroupUsers.PSEUDO_USER;
	
	ServiceContext svcctx ;
	@Autowired
    private CabinetDAO cabinetdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		

		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId( IdKey.CABINET, Long.class);
			
		CabinetInfo info = new CabinetInfo();
		info.setInfoId(id);


		info.setWorkgroupId(345l);
		info.setCabinetName("cab1000...");
		info.setDescription("descr0001010");
		info.setVersionable(true);
		info.setCapacity(456l);
		info.setStorageId(2);
		info.setCreateDate(new Date(System.currentTimeMillis()));
		info.setCreator("cr0001");
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		cabinetdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		info.setDescription("sub0001");
		int c = cabinetdao.update( info,FilterMode.NONE);
		System.out.println("--- update done:"+c);
		
		CabinetInfo info2= cabinetdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = cabinetdao.delete( id);
		System.out.println("--- delete done:"+d);
	}

	@Test
	public void test1() throws Exception{

		svcctx = new ServiceContext(principal);
		for(int i = 0;i<5;i++){
			InfoId<Long> id = idService.generateId( IdKey.CABINET,Long.class);
				
			CabinetInfo info = new CabinetInfo();
			info.setInfoId(id);


			info.setWorkgroupId(345l);
			info.setCabinetName("cab1000...");
			info.setDescription("descr0001010");
			info.setVersionable(true);
			info.setCapacity(456l);
			info.setStorageId(3);
			info.setCreateDate(new Date(System.currentTimeMillis()));
			info.setCreator("cr0001");
					
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			cabinetdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
