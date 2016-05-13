package com.gp.storage;

import org.apache.commons.lang.StringUtils;

/**
 * The content range derive from the HTTP Content-Range information. <br>
 * Content-Range : bytes 21010-47021/47022 
 * 
 * @author gary diao
 * @version 0.1 2015-12-12
 * 
 **/
public class ContentRange {

	private Long startPos = 0l;
	
	private Long endPos = 0l;
	
	private Long fileSize = 0l;
	
	public ContentRange (){
	}
	
	public ContentRange (Long startPos, Long endPos, Long fileSize){
		this.startPos = startPos;
		this.endPos = endPos;
		this.fileSize = fileSize;
	}

	public Long getStartPos() {
		return startPos;
	}

	public void setStartPos(Long startPos) {
		this.startPos = startPos;
	}

	public Long getEndPos() {
		return endPos;
	}

	public void setEndPos(Long endPos) {
		this.endPos = endPos;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	
	public int getRangeLength(){
		return (int)(this.endPos - this.startPos) + 1;
	}
	
    /**
     * content range : bytes 21010-47021/47022 
     **/
	public static ContentRange parse(String contentRange){
		
		if(StringUtils.isBlank(contentRange))
			return null;
		
		// :21010-47021/47022 
		contentRange = contentRange.substring(contentRange.indexOf(' ')+1);
    	// :47022
    	String lengthStr = contentRange.substring(contentRange.indexOf('/')+1);
    	// :21010
    	String startStr = contentRange.substring(0, contentRange.indexOf('-'));
    	// :47021
    	String endStr = contentRange.substring(contentRange.indexOf('-')+1,contentRange.indexOf('/'));
    	
    	ContentRange range = new ContentRange();
    	range.setStartPos(Long.valueOf(startStr));
    	range.setEndPos(Long.valueOf(endStr));
    	range.setFileSize(Long.valueOf(lengthStr));
    	
    	return range;
	}
}
