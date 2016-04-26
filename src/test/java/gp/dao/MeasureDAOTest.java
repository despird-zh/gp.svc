package gp.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.gp.common.IdKey;
import com.gp.common.Measures;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.common.Users;
import com.gp.dao.MeasureDAO;
import com.gp.dao.MessageDAO;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.info.MeasureInfo;
import com.gp.info.MessageInfo;
import com.gp.svc.IdService;

@ContextConfiguration(locations = "/mysql-test.xml")
public class MeasureDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = Users.PESUOD_USER;
	@Autowired
    private MeasureDAO measuredao;
	
	@Autowired
    private IdService idService;
	
	@Test
	public void test() throws Exception{
		System.out.println("Test not after");
		InfoId<Long> trcid = IdKey.WORKGROUP.getInfoId(1l);
		FlatColLocator[] columns = new FlatColLocator[]{Measures.WORKGROUP_KPI1,Measures.WORKGROUP_KPI2,Measures.WORKGROUP_KPI3};
		
		Date after = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-04-25 22:06:25");
		
		List<MeasureInfo> minfos = measuredao.queryListAfter(trcid, "T", after, columns);
		
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
		FlatColLocator[] columns = new FlatColLocator[]{Measures.WORKGROUP_KPI1,Measures.WORKGROUP_KPI2,Measures.WORKGROUP_KPI3};
		
		Date before = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-04-25 18:12:35");
		
		List<MeasureInfo> minfos = measuredao.queryListBefore(trcid, "T", before, columns);
		
		if(CollectionUtils.isNotEmpty(minfos)){
			for(MeasureInfo minfo : minfos)
				System.out.println("--- id : " + minfo.getInfoId());
		}else{
			System.out.println("Not Found any rows");
		}
	}
}
