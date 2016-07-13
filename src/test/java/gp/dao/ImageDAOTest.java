package gp.dao;

import java.io.File;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.config.TestConfig;
import com.gp.common.GeneralConfig;
import com.gp.common.GeneralConstants;
import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.common.SystemOptions;
import com.gp.common.FlatColumns.FilterMode;
import com.gp.dao.ImageDAO;
import com.gp.dao.TaskDAO;
import com.gp.dao.TaskRouteDAO;
import com.gp.dao.VoteDAO;
import com.gp.dao.WorkgroupDAO;
import com.gp.info.ImageInfo;
import com.gp.info.InfoId;
import com.gp.info.TaskInfo;
import com.gp.info.TaskRouteInfo;
import com.gp.info.VoteInfo;
import com.gp.info.WorkgroupInfo;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class ImageDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = new Principal("demouser");
	ServiceContext svcctx ;
	@Autowired
    private ImageDAO imgdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		
		svcctx = new ServiceContext(principal);
		
		InfoId<Long> id = (InfoId<Long>)idService.generateId(principal.getAccount(), IdKey.IMAGE,Long.class);

		ImageInfo info = new ImageInfo(GeneralConfig.getString(SystemOptions.IMAGE_CACHE_PATH));
		info.setInfoId(id);
		
		info.setFormat("png");
		info.setImageFile(new File("d:\\avatar5.png"));
		info.setModifier("modifer001");
		info.setModifyDate(new Date(System.currentTimeMillis()));
		
		imgdao.create( info);
		System.out.println("--- create done:"+id.toString());
		
		int c = imgdao.update( info,FilterMode.NONE);
		System.out.println("--- update done:"+c);
		
		ImageInfo info2= imgdao.query( id);
		System.out.println("--- query done:"+info2.toString());
		
		int d = imgdao.delete( id);
		System.out.println("--- update done:"+d);
	}
	
}
