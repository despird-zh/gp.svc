package gp.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp.common.Cabinets;
import com.gp.util.CommonUtils;

public class Set2Json {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException{
		
		ObjectMapper mapper = new ObjectMapper();
		String json = "[\"name\", \"name1\"]";
		Set<String> list = mapper.readValue(json, new TypeReference<Set<String>>(){});
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("prop1", "属性值1");
		map.put("prop2", "属性值2");
		map.put("prop3", "属性值3");
		map.put("prop4", "属性值4");
		map.put("prop5", 345);
		
		String mstr = CommonUtils.toJson(map);
		
		System.out.println(mstr);
	}
}
