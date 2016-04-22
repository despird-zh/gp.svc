package com.gp.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Images {
	
	public static SimpleDateFormat FNAME_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");
	
	// match "123-20160201-123213.jpg" pattern 
	public static String FNAME_REGEX = "^\\d+-\\d{8}-\\d{6}\\.\\w+$";
	
	static Pattern FNAME_PATTERN = Pattern.compile(FNAME_REGEX);  
	
	/**
	 * Get the image file name 
	 * @param date The image create date
	 * @param id The id of image
	 * @param extension The image extension 
	 **/
	public static String getImgFileName(Date touchdate, Long id, String extension){
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(id).append('-').append(FNAME_DATE_FORMAT.format(touchdate));
		sbuf.append('.').append(extension);
		
		return  sbuf.toString();
	}
	
	/**
	 * parse the create date from the file name 
	 * 
	 * @param filename The File name
	 **/
	public static Date parseTouchDate(String filename){
		
		if(null == filename || !FNAME_PATTERN.matcher(filename).matches()){
			return null;
		}
		String datepart = filename.substring(filename.indexOf('-') + 1, filename.lastIndexOf('.'));
		try {
			return FNAME_DATE_FORMAT.parse(datepart);
		} catch (ParseException e) {
			
			// ignore
		}
		return null;
	}
	
	/**
	 * Parse the image id from the file name 
	 **/
	public static Long parseImageId(String filename){

		if(null == filename || !FNAME_PATTERN.matcher(filename).matches()){
			return null;
		}
		String idpart = filename.substring(0, filename.indexOf('-'));
		return Long.valueOf(idpart);
	}
	
	/**
	 * Check the filename is qualified or not 
	 **/
	public static boolean isQualifiedName(String filename){
		
		if(null != filename && FNAME_PATTERN.matcher(filename).matches()){
			return true;
		}else
			return false;
	}
}
