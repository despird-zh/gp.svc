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
import com.gp.dao.BinaryDAO;
import com.gp.dao.info.BinaryInfo;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class BinaryDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = GroupUsers.PSEUDO_USER;
	ServiceContext svcctx ;
	@Autowired
    private BinaryDAO binarydao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		

		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = idService.generateId(IdKey.BINARY, Long.class);
				
		BinaryInfo info = new BinaryInfo();
		info.setInfoId(id);


		info.setSize(345l);
		info.setHashCode("sdekdxlsoe009283lxx");
		info.setStoreLocation("demo dlocation blalslslsllsls..");
		info.setState("down");
		info.setCreator("creator1");
		info.setCreateDate(new Date(System.currentTimeMillis()));
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		binarydao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		info.setHashCode("ss");
		int c = binarydao.update(  info,FilterMode.NONE);
		System.out.println("--- update done:"+c);
		
		BinaryInfo info2= binarydao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = binarydao.delete( id);
		System.out.println("--- delete done:"+d);
	}

}
