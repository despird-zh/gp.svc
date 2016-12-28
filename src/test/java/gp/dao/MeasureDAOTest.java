package gp.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.config.TestConfig;
import com.gp.common.IdKey;
import com.gp.common.Measures;
import com.gp.common.Principal;
import com.gp.common.GroupUsers;
import com.gp.dao.MeasureDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.dao.info.MeasureInfo;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class MeasureDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = GroupUsers.PSEUDO_USER;
	@Autowired
    private MeasureDAO measuredao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		System.out.println("Test not after");
		InfoId<Long> trcid = IdKey.WORKGROUP.getInfoId(1l);
		FlatColLocator[] columns = new FlatColLocator[]{Measures.WG_MEAS_FILE,Measures.WG_MEAS_EXT_MBR,Measures.WG_MEAS_SUB_GRP};
		
		Date after = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-04-25 22:06:25");
		
		List<MeasureInfo> minfos = measuredao.queryAfter(trcid, "T", after, columns);
		
		if(CollectionUtils.isNotEmpty(minfos)){
			for(MeasureInfo minfo : minfos)
				System.out.println("--- id : " + minfo.getInfoId());
		}else{
			System.out.println("Not Found any rows");
		}
	}
	
	@Test
	public void test1() throws Exception{
		System.out.println("Test not before");
		InfoId<Long> trcid = IdKey.WORKGROUP.getInfoId(1l);
		FlatColLocator[] columns = new FlatColLocator[]{Measures.WG_MEAS_FILE,Measures.WG_MEAS_EXT_MBR,Measures.WG_MEAS_SUB_GRP};
		
		Date before = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-04-25 18:12:35");
		
		List<MeasureInfo> minfos = measuredao.queryBefore(trcid, "T", before, columns);
		
		if(CollectionUtils.isNotEmpty(minfos)){
			for(MeasureInfo minfo : minfos)
				System.out.println("--- id : " + minfo.getInfoId());
		}else{
			System.out.println("Not Found any rows");
		}
	}
}
