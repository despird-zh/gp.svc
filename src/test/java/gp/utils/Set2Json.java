package gp.utils;

import java.io.IOException;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Set2Json {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException{
		
		ObjectMapper mapper = new ObjectMapper();
		String json = "[\"name\", \"name1\"]";
		Set<String> list = mapper.readValue(json, new TypeReference<Set<String>>(){});
		System.out.println(list);
	}
}
