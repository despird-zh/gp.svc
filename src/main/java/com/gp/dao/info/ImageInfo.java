package com.gp.dao.info;

import java.io.File;
import com.gp.common.Images;
import com.gp.info.TraceableInfo;

/**
 * Store image file information in database
 * 
 **/
public class ImageInfo extends TraceableInfo<Long> {

	private static final long serialVersionUID = 1L;

	private String parentPath = null;

	private String format = null;
	
	private String imageName = null;
	
	private String persist = null;
	
	private String category = null;
	
	private String link = null;
	
	private File dataFile = null;
	
	/**
	 * Constructor with parentPath
	 * 
	 * @param parentPath The parent path to store the file data 
	 **/
	public ImageInfo(String parentPath){
		
		this.parentPath = parentPath;
	}
	
	/**
	 * Constructor with default temporary path
	 * the parent path : System.getProperty("java.io.tmpdir")
	 **/
	public ImageInfo(){
		this.parentPath = System.getProperty("java.io.tmpdir");
	}
	
	/**
	 * Get the image data file object, if the dataFile is null, use the default file name and parent path to create 
	 * a default file. otherwise return the file object.
	 * 
	 * @return the file on disk which hold the image binary data.
	 **/
	public File getDataFile() {
		if(null == dataFile)
			dataFile = new File(getFilePath());
		
		return dataFile;
	}
	
	/**
	 * Set the image file object
	 * @param imageFile the image file object to be persisted 
	 **/
	public void setDataFile(File imageFile) {
		this.dataFile = imageFile;
	}

	/**
	 * Get the format of file, i.e doc,xls etc. 
	 **/
	public String getFormat() {
		return format;
	}

	/**
	 * Set the format of file, i.e doc, xls etc. 
	 **/
	public void setFormat(String format) {
		this.format = format;
	}
	
	/**
	 * Get the path string of file.
	 * as for database persisted images, it is {parentPath} + {link};
	 * other images, it equals {parentPath} + {file name, pattern : {id}-{yyyyMMdd}-{HHmmss}.{extension}}
	 **/
	public String getFilePath(){
		String fn = null;
		if(Images.Persist.DATABASE.name().equals(this.persist)){
			fn = link;
		}else{
			fn = Images.getImgFileName(getModifyDate(), getInfoId().getId(), format);
		}
		return parentPath + File.separator + fn;
	}
	
	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String name) {
		this.imageName = name;
	}

	public String getPersist() {
		return persist;
	}

	public void setPersist(String persist) {
		this.persist = persist;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
