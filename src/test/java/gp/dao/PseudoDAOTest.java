package gp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.ParsedSql;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.gp.common.Principal;
import com.gp.common.ServiceContext;
import com.gp.dao.PseudoDAO;
import com.gp.svc.CommonService;

@ContextConfiguration(locations = "/mysql-test.xml")
public class PseudoDAOTest extends AbstractJUnit4SpringContextTests{

	Principal principal = new Principal("demouser");
	ServiceContext svcctx ;
	@Autowired
    private PseudoDAO pseudodao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() {
		
		String namedSql = "select 3  from dual where 1 in (:p1) and :priv";
		
		ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(namedSql);

	    List<Integer> parameters = new ArrayList<Integer>();
	    for (int a : new int[]{1,2})
	        parameters.add(a);

	    Map<String,Object> map = new HashMap<String,Object>();
	    map.put("p1", parameters);
	    map.put("priv", "3&1=1");

		svcctx = new ServiceContext(principal);
		
		try {

			NamedParameterJdbcTemplate jt = pseudodao.getJdbcTemplate(NamedParameterJdbcTemplate.class);
			int c = jt.queryForObject(namedSql, map, Integer.class);
			System.out.println("-- the output is : " + c);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
