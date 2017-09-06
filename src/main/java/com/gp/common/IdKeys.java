package com.gp.common;

import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gp.info.Identifier;
import com.gp.info.InfoId;

public class IdKeys {
	
	/**
	 * the custom Identifier set 
	 **/
	private static Set<Identifier> IdSet = new HashSet<Identifier>();

	/** 
	 * Finds the value of the given enumeration by name, case-insensitive. 
	 * Throws an IllegalArgumentException if no match is found.  
	 **/
	public static Identifier valueOfIgnoreCase(String name) {
		
		IdKey idKey = null;
		if(StringUtils.isBlank(name)) return null;
		try {
			idKey = IdKey.valueOf(name.toUpperCase());
			return idKey;
		}catch(Exception e) {
			// ignore
		}
	    for (IdKey enumValue : IdKey.values()) {
	        if (enumValue.getSchema().equalsIgnoreCase(name)) {
	            return enumValue;
	        }
	    }
	    for (Identifier enumValue : IdSet) {
	        if (enumValue.getSchema().equalsIgnoreCase(name)) {
	            return enumValue;
	        }
	    }
	    throw new IllegalArgumentException(String.format(
	            "There is no value with name '%s' in Enum IdKey",name
	        ));
	}
	
	/**
	 * Generate the trace code with node code and info id
	 * @param nodeCode the node code
	 * @param infoId the id of info record
	 *  
	 **/
	public static String getTraceCode(String nodeCode, InfoId<?> infoId) {
		
		Encoder encoder = Base64.getEncoder();
		StringBuffer sb = new StringBuffer(30);
		sb.append(nodeCode).append(GeneralConstants.NAMES_SEPARATOR);
		sb.append(infoId.toString());
		
		return encoder.encodeToString(sb.toString().getBytes());
	}
	
	/**
	 * Parse the node code
	 * @param traceCode the trace code
	 **/
	public static String getNodeCode(String traceCode) {
		Decoder decoder = Base64.getDecoder();
		
		String fullOrigin = new String(decoder.decode(traceCode));
		int idx = fullOrigin.indexOf(GeneralConstants.NAMES_SEPARATOR);
		
		return fullOrigin.substring(0, idx);
	}
	
	/**
	 * Parse the info id from the trace code
	 * 
	 * @param traceCode the trace code
	 * @param clazz the class of InfoId id value
	 **/
	public static <M> InfoId<M> getInfoId(String traceCode) {
		Decoder decoder = Base64.getDecoder();
		
		String fullOrigin = new String(decoder.decode(traceCode));
		int idx = fullOrigin.indexOf(GeneralConstants.NAMES_SEPARATOR);
		
		return IdKeys.parseInfoId(fullOrigin.substring(idx+1));
	}
	
	/**
	 * Get the custom Identifier set
	 * 
	 * @return Set the identifier set
	 * 
	 **/
	public static Set<Identifier> getIdentifierSet(){
		
		return IdSet;
	}
	
	/**
	 * Add the custom Identifier
	 * 
	 * @param idkeys the keys to be added into set
	 * 
	 **/
	public static void addIdentifier(Identifier... idkeys){
		
		if(ArrayUtils.isEmpty(idkeys)) return;
		
		for(Identifier idkey: idkeys)
			IdSet.add(idkey);
	}
	
	/**
	 * Get the InfoId object from the Identitifer and sequence
	 * 
	 * @param identifier the ID 
	 * @param sequence the sequence 
	 * 
	 **/	
	public static <T> InfoId<T> getInfoId(Identifier identifier, T sequence) {
		
		if(sequence == null || !identifier.getIdClass().equals(sequence.getClass()))
			throw new UnsupportedOperationException("Sequence type is not supported, require:"+ identifier.getIdClass().getName() + " type parameter!");
		
		return new InfoId<T>(identifier,sequence);
	}
	
	/**
	 * Get the InfoId object from the Identitifer and sequence
	 * 
	 * @param identifier the ID 
	 * @param sequence the sequence 
	 * 
	 **/	
	public static <T> InfoId<T> getInfoId(String idKeyName, T sequence) {
		
		Identifier idf = valueOfIgnoreCase(idKeyName);
		
		if(null == idf) {
			return null;
		}
		else {
			return getInfoId(idf, sequence);
		}
	}
	

	/**
	 * Make ObjectMapper support the InfoId module 
	 * @param mapper
	 **/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ObjectMapper withInfoIdModule(final ObjectMapper mapper) {
		
		final SimpleModule module = new SimpleModule("InfoIdSerializeModule");
		
		JsonSerializer<InfoId> serializer = new InfoIdSerializer();
		JsonDeserializer deserializer = new InfoIdDeserializer();
	    module.addDeserializer(InfoId.class, deserializer);
	    module.addSerializer(InfoId.class, serializer);
	    
	    mapper.registerModule(module);
		return mapper;
	}
	
	/**
	 * The serializer to support InfoId 
	 **/
	@SuppressWarnings("rawtypes")
	public static class InfoIdSerializer extends JsonSerializer<InfoId>{

		@Override
		public void serialize(InfoId infoId, JsonGenerator jsonGenerator, SerializerProvider arg2)
				throws IOException, JsonProcessingException {
			
			jsonGenerator.writeString(infoId.toString());
		}
	}
	
	/**
	 * the deserializer to support InfoId
	 **/
	@SuppressWarnings("rawtypes")
	public static class InfoIdDeserializer extends JsonDeserializer<InfoId>{

		@Override
		public InfoId deserialize(JsonParser parser, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			String fullStr = parser.getValueAsString();
			InfoId<?> rtv = parseInfoId(fullStr);
			return rtv;
		}
		
	}

	/**
	 * parse the string into InfoId object, format is {type}:{id} ,e.g audit:a000101 
	 * 
	 * @param idstr the id string get from InfoId.toString()
	 * @param idclazz the class of Id
	 **/
	@SuppressWarnings("unchecked")
	public static <M> InfoId<M> parseInfoId(String idstr){
		
		if(StringUtils.isBlank(idstr)) return null;
		
		String[] parts = StringUtils.split(idstr, GeneralConstants.KEYS_SEPARATOR);
		Identifier idkey = valueOfIgnoreCase(parts[0]);
		
		M id = null;
		Class<?> idclazz = idkey.getIdClass();
		if(parts.length < 2){
			
			if(Integer.class.equals(idclazz))
				id = (M)new Integer(-1);
			
			else if(Long.class.equals(idclazz))
				id = (M)new Long(-1);
			
			else if(String.class.equals(idclazz))
				id = (M)new String();
			
			return new InfoId<M>(idkey, id);
		}
		
		if(parts.length == 2 ){
			
			if(Integer.class.equals(idclazz))
				id = (M)Integer.valueOf(parts[1]);
			
			else if(Long.class.equals(idclazz))
				id = (M)Long.valueOf(parts[1]);
			
			else if(String.class.equals(idclazz))
				id = (M)parts[1];
			
		}
				
		return new InfoId<M>(idkey,id);	
	}

	/**
	 * check if the InforId is valid 
	 * @param the id object to be checked
	 * @return true valid; false invalid
	 **/
	public static boolean isValidId(InfoId<?> id){
		
		if(id == null){ 
			
			return false;
		}else if(id.getId() == null ){	
			
			return false;
			
		}else if(ObjectUtils.equals(id.getId(), GeneralConstants.LOCAL_SOURCE )||
				ObjectUtils.equals(id.getId(), GeneralConstants.PERSONAL_WORKGROUP) ||
				ObjectUtils.equals(id.getId(), GeneralConstants.ORGHIER_WORKGROUP) ||
				ObjectUtils.equals(id.getId(), GeneralConstants.ORGHIER_ROOT) ||
				ObjectUtils.equals(id.getId(), GeneralConstants.FOLDER_ROOT) ){
			
			return true;
			
		}else if( id.getId() instanceof Integer && (Integer)(id.getId()) < 1){
			
			return false;
		}else if( id.getId() instanceof Long && (Long)(id.getId()) < 1){
			
			return false;
		}
		
		return true;
	}
}
