package gp.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.config.TestConfig;
import com.gp.common.FlatColumns;
import com.gp.common.IdKey;
import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.common.Users;
import com.gp.dao.DictionaryDAO;
import com.gp.info.DictionaryInfo;
import com.gp.info.FlatColLocator;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;

@ContextConfiguration(classes={TestConfig.class})
public class DictDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = Users.PESUOD_USER;

	@Autowired
    private DictionaryDAO dictdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		

		InfoId<Long> id = (InfoId<Long>)idService.generateId(principal.getAccount(), IdKey.DICTIONARY,Long.class);
		
		DictionaryInfo info = new DictionaryInfo();
		info.setInfoId(id);

		info.setGroup("dgroup");
		info.setKey("dk001");
		info.setValue("dv001");
		info.setDefaultLang("zh_cn");
		Map<FlatColLocator, String> labelMap = new HashMap<FlatColLocator, String>();
		labelMap.put(FlatColumns.COL_DICT_DE_DE, "de demo");
		labelMap.put(FlatColumns.COL_DICT_EN_US, "this demo");
		labelMap.put(FlatColumns.COL_DICT_FR_FR, "franch demo");
		labelMap.put(FlatColumns.COL_DICT_ZH_CN, "测试");
		labelMap.put(FlatColumns.COL_DICT_RU_RU, "this russia");
		
		info.setLabelMap(labelMap);
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

		principal.setAccount("acc001");
		for(int i = 0;i<5;i++){
			InfoId<Long> id = (InfoId<Long>)idService.generateId(principal.getAccount(), IdKey.DICTIONARY,Long.class);
			
			DictionaryInfo info = new DictionaryInfo();
			info.setInfoId(id);

			info.setGroup("dgroup");
			info.setKey("dk001");
			info.setValue("dv001");
			
			info.setDefaultLang("zh_cn");
			Map<FlatColLocator, String> labelMap = new HashMap<FlatColLocator, String>();
			labelMap.put(FlatColumns.COL_DICT_DE_DE, "de demo");
			labelMap.put(FlatColumns.COL_DICT_EN_US, "this demo");
			labelMap.put(FlatColumns.COL_DICT_FR_FR, "franch demo");
			labelMap.put(FlatColumns.COL_DICT_ZH_CN, "测试");
			labelMap.put(FlatColumns.COL_DICT_RU_RU, "this russia");
			
			info.setLabelMap(labelMap);
			info.setModifier("modifer001");
			info.setModifyDate(new Date(System.currentTimeMillis()));
			
			dictdao.create( info);
			System.out.println("--- create done:"+id.toString());
		}
	}
}
