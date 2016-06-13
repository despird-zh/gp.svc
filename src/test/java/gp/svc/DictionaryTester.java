package gp.svc;

import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.config.TestConfig;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.common.GroupUsers;
import com.gp.info.DictionaryInfo;
import com.gp.svc.DictionaryService;

@ContextConfiguration(classes={TestConfig.class})
public class DictionaryTester extends AbstractJUnit4SpringContextTests{
	
	Principal principal = GroupUsers.PSEUDO_USER;
	ServiceContext svcctx ;

	@Autowired
    private DictionaryService dictionaryService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);

		DictionaryInfo dinfo = dictionaryService.getDictEntry("prop.storageid", false);
		
		System.out.println("-- 1 --"+dinfo.getGroup());
		
	}
}
