package com.gp.common;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;

import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import com.gp.info.Identifier;
import com.gp.info.InfoId;
import com.gp.info.InfoIds;

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

	    for (IdKey enumValue : IdKey.values()) {
	        if (enumValue.name().equalsIgnoreCase(name) 
	        		|| enumValue.getSchema().equalsIgnoreCase(name)) {
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
	public static String parseNodeCode(String traceCode) {
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
	public static <M> InfoId<M> parseInfoId(String traceCode, Class<M> clazz) {
		Decoder decoder = Base64.getDecoder();
		
		String fullOrigin = new String(decoder.decode(traceCode));
		int idx = fullOrigin.indexOf(GeneralConstants.NAMES_SEPARATOR);
		
		return InfoIds.parseInfoId(fullOrigin.substring(idx+1), clazz);
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
		
		return new InfoId<T>(identifier.getSchema(), identifier.getIdColumn(), sequence);
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
}
