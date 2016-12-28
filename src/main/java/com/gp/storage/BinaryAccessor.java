package com.gp.storage;

import java.io.InputStream;
import java.io.OutputStream;

import com.gp.dao.BinaryDAO;
import com.gp.dao.StorageDAO;
import com.gp.exception.StorageException;
import com.gp.info.InfoId;

/**
 * This class defines the basic methods of binary accessor class, these method to be implemented as per final store way.
 * 
 * @author gary diao
 * @version 0.1 2015-12-12
 * 
 **/
public abstract class BinaryAccessor {

	/** the storage dao */
	private StorageDAO storagedao;
	/** the binary dao */
	private BinaryDAO binarydao;
	
	/**
	 * constructor with dao implementations, storage dao and binary dao. 
	 **/
	public BinaryAccessor(StorageDAO storagedao, BinaryDAO binarydao){
		
		this.storagedao = storagedao;
		this.binarydao = binarydao;
	}
	
	/**
	 * get the storage dao
	 **/
	protected StorageDAO getStorageDAO(){
		
		return this.storagedao;
	}
	
	/**
	 * get the binary dao 
	 **/
	protected BinaryDAO getBinaryDAO(){
		
		return this.binarydao;
	}
	
	/**
	 * Fill the binary chunk data with specified chunk of binary.
	 * 
	 * @param binaryId the id the binary
	 * @param setting the setting of storage
	 * @param path the relative path under storage.
	 * @param range the expected data range
	 * @param source the source stream
	 **/
	public abstract void fillBinary(InfoId<Long> binaryId, StorageSetting setting, String path, ContentRange range,InputStream source) throws StorageException;
	
	/**
	 * Dump the binary chunk to binary store
	 * 
	 * @param binaryId the id the binary
	 * @param setting the setting of storage
	 * @param path the relative path under storage.
	 * @param range the expected data range
	 * @param source the target stream
	 **/
	public abstract void dumpBinary(InfoId<Long> binaryId, StorageSetting setting, String path, ContentRange range,OutputStream target)throws StorageException;
	
	/**
	 * Fill the binary store with specified stream.
	 * 
	 * @param binaryId the id the binary
	 * @param setting the setting of storage
	 * @param path the relative path under storage.
	 * @param source the input stream of data 
	 *  
	 **/
	public abstract void fillBinary(InfoId<Long> binaryId, StorageSetting setting, String path, InputStream source)throws StorageException;
	
	/**
	 * Dump the binary to output stream
	 * 
	 * @param binaryId the id the binary
	 * @param setting the setting of storage
	 * @param path the relative path under storage.
	 * @param target output stream to accept binary data
	 **/
	public abstract void dumpBinary(InfoId<Long> binaryId, StorageSetting setting, String path, OutputStream target)throws StorageException;
	
}
