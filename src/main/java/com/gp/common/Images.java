/*******************************************************************************
 * Copyright 2016 Gary Diao - gary.diao@outlook.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
	 * Get the image file name, pattern : {id}-{yyyyMMdd}-{HHmmss}.{extension}
	 * 
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
	 * Get the image file name, pattern : {id}-{yyyyMMdd}-{HHmmss}.{extension}
	 *
	 * @param id The id of image
	 * @param extension The image extension
	 **/
	public static String getImgFileName(Long id, String extension){

		Date touchdate = new Date(System.currentTimeMillis());
		return getImgFileName(touchdate, id, extension);
	}

	/**
	 * parse the create date from the file name,pattern : {id}-{yyyyMMdd}-{HHmmss}.{extension}
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
	 * Parse the image id from the file name, pattern : {id}-{yyyyMMdd}-{HHmmss}.{extension}
	 **/
	public static Long parseImageId(String filename){

		if(null == filename || !FNAME_PATTERN.matcher(filename).matches()){
			return null;
		}
		String idpart = filename.substring(0, filename.indexOf('-'));
		return Long.valueOf(idpart);
	}
	
	/**
	 * Check the filename is qualified or not ,pattern : {id}-{yyyyMMdd}-{HHmmss}.{extension}
	 * @param filename the name of file
	 **/
	public static boolean isQualifiedName(String filename){
		
		if(null != filename && FNAME_PATTERN.matcher(filename).matches()){
			return true;
		}else
			return false;
	}
	
	/**
	 * Define the category of image 
	 **/
	public static enum Category{
		
		WGROUP_AVATAR, // the workgroup avatar image
		USER_AVATAR, // the user avatar image
		POST_EXCERPT,
	}
	
	public static enum Persist{
		
		DATABASE,
		HBASE
	}
}
