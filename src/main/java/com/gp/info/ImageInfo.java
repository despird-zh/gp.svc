package com.gp.info;

import java.io.File;
import java.util.Date;

import com.gp.common.Images;

/**
 * Store image file information in database
 * 
 **/
public class ImageInfo extends TraceableInfo<Long>{

	private static final long serialVersionUID = 1L;

	private String parentPath = null;

	private String format = null;
	
	private String imageName = null;
	
	private String extension = null;
	
	private File imageFile = null;

	private Date touchTime = null;

	/**
	 * Constructor with parentPath
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
	 * Get the image file object 
	 * @return the file on disk
	 **/
	public File getImageFile() {
		if(null == imageFile)
			imageFile = new File(getFilePath());
		
		return imageFile;
	}
	
	/**
	 * Set the image file object
	 * @param imageFile the image file object to be persisted 
	 **/
	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
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
	 * Get the path string of file, it equals {parentPath} + {createTime:yyyyMMdd-HHmmss} + [-] + {fileid} + {extension}
	 **/
	public String getFilePath(){
		
		String fn = Images.getImgFileName(touchTime, getInfoId().getId(), extension);
		return parentPath + File.separator + fn;
	}
	
	/**
	 * Get the file name combine with touch data/ id / extension 
	 **/
	public String getFileName(){
		
		return Images.getImgFileName(touchTime, getInfoId().getId(), extension);
	}
	/**
	 * Get the extension of file 
	 **/
	public String getExtension() {
		return extension;
	}

	/**
	 * Set the extension of file 
	 **/
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	/**
	 * Get the create time in Long value
	 **/
	public Date getTouchTime() {
		return touchTime;
	}

	/**
	 * Set the create time in Long value 
	 **/
	public void setTouchTime(Date touchTime) {
		this.touchTime = touchTime;
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
}
