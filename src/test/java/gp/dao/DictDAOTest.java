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
import com.gp.dao.DictionaryDAO;
import com.gp.info.DictionaryInfo;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;

@ContextConfiguration(locations = "/mysql-test.xml")
public class DictDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = Users.PESUOD_USER;
	ServiceContext svcctx ;
	@Autowired
    private DictionaryDAO dictdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		

		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = (InfoId<Long>)idService.generateId(principal.getAccount(), IdKey.DICTIONARY,Long.class);
		
		DictionaryInfo info = new DictionaryInfo();
		info.setInfoId(id);

		info.setGroup("dgroup");
		info.setKey("dk001");
		info.setValue("dv001");
		info.setLabel("{}");
		info.setLanguage("zh_cn");
		
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		dictdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		info.setValue("sub00011");
		int c = dictdao.update( info);
		System.out.println("--- update done:"+c);
		
		DictionaryInfo info2= dictdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = dictdao.delete( id);
		System.out.println("--- delete done:"+d);
	}
	
	@Test
	public void test1() throws Exception{
		
		svcctx = new ServiceContext(principal);
		principal.setAccount("acc001");
		for(int i = 0;i<5;i++){
			InfoId<Long> id = (InfoId<Long>)idService.generateId(principal.getAccount(), IdKey.DICTIONARY,Long.class);
			
			DictionaryInfo info = new DictionaryInfo();
			info.setInfoId(id);

			info.setGroup("dgroup");
			info.setKey("dk001");
			info.setValue("dv001");
			info.setLabel("{}");
			info.setLanguage("zh_cn");
			
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			dictdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
