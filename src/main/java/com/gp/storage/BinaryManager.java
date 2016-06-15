package com.gp.storage;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gp.common.IdKey;
import com.gp.common.SpringContextUtil;
import com.gp.common.Storages;
import com.gp.dao.BinaryDAO;
import com.gp.dao.StorageDAO;
import com.gp.exception.StorageException;
import com.gp.info.BinaryInfo;
import com.gp.info.InfoId;
import com.gp.info.StorageInfo;

/**
 * Binary manager provides the method to interact with binary storage.
 * Basically it extracts the information i.e. storageid, binaryid, path from uri pattern binary location string.
 * Then hand over the call to respective binary accessor as per storage type of binary. 
 * <p>
 * Internally it holds the storage and binary dao implementation to read/write database. </p>
 * <p>
 * It implements singleton pattern, use BinaryManager.instance() to acquire object.
 * </p>
 * 
 * @author gary diao
 * @version 0.1 2015-12-12
 * 
 **/
public class BinaryManager {

	static Logger LOGGER = LoggerFactory.getLogger(BinaryManager.class);
	
	/** the storage dao object */
	private StorageDAO storagedao; 
	/** the binary dao object */
	private BinaryDAO binarydao;
	/** the disk storage accessor */
	private DiskBinaryAccessor diskaccessor = null;
	
	/**
	 * hidden default constructor 
	 **/
	private BinaryManager(){
		
		storagedao = SpringContextUtil.getSpringBean(StorageDAO.class);		
		binarydao = SpringContextUtil.getSpringBean(BinaryDAO.class);
		
		diskaccessor = new DiskBinaryAccessor(storagedao, binarydao);
		
		LOGGER.debug("Bind storage dao : {}, binary dao : {} ", 
				(null == storagedao ? "fail":"success"), 
				(null == binarydao ? "fail":"success"));
	}
	
	/**
	 * singleton instance of manager 
	 **/
	private static BinaryManager instance = null;

	/**
	 * get the instance of binary manager 
	 **/
	public static BinaryManager instance(){
		
		if(null == instance){
			instance = new BinaryManager();			
		}
		return instance;
	}
	
	/**
	 * fill the chunk of target binary with the specified chunk data.
	 * ChunkBuffer use {@link ByteBuffer} store byte data of file chunk.
	 *  
	 * @param binaryKey the key of binary record
	 * @param range the content range to be written
	 * @param input the source data stream
	 *   
	 **/
	public void fillBinary(InfoId<Long> binaryKey, ContentRange range, InputStream input) throws StorageException{
		
		BinaryInfo binfo = binarydao.query(binaryKey);
		String binaryURI = binfo.getStoreLocation();
		fillBinary(binaryURI, input);
	}
	
	/**
	 * fill the chunk of target binary with the specified chunk data.
	 * ChunkBuffer use {@link ByteBuffer} store byte data of file chunk.
	 *  
	 * @param binaryURI the uri string of target binary
	 * @param range the content range to be written
	 * @param input the source data stream
	 **/
	public void fillBinary(String binaryURI, ContentRange range, InputStream input) throws StorageException{
		
		BinUriMeta meta = new BinUriMeta(binaryURI);	
		BinaryAccessor binaccessor = null;
		
		StorageInfo sinfo = storagedao.query(meta.StorageId);
		if(null == sinfo)
			throw new StorageException("the storage({}) not exist.", meta.StorageId.toString());
		
		StorageSetting storagesetting = new StorageSetting(sinfo.getSettingJson());
		String rootpath = meta.BinPath;
		
		if(Storages.StorageType.DISK.name().equalsIgnoreCase(sinfo.getStorageType())){
			
			binaccessor = this.diskaccessor;
		}
		
		binaccessor.fillBinary(meta.BinaryId, storagesetting, rootpath, input);		
	}
	
	/**
	 * fill the binary with the specified stream
	 * 
	 * @param binaryKey the key of binary record
	 * @param source the input stream
	 *   
	 **/
	public void fillBinary(InfoId<Long> binaryKey, InputStream source)throws StorageException{
		BinaryInfo binfo = binarydao.query(binaryKey);
		String binaryURI = binfo.getStoreLocation();
		fillBinary(binaryURI, source);
	}
	
	/**
	 * fill the binary with the specified stream
	 * 
	 * @param binaryURI the uri string of binary
	 * @param source the input stream
	 *   
	 **/
	public void fillBinary(String binaryURI, InputStream source)throws StorageException{
		
		BinUriMeta meta = new BinUriMeta(binaryURI);	
		BinaryAccessor binaccessor = null;
		
		StorageInfo sinfo = storagedao.query(meta.StorageId);
		StorageSetting storagesetting = new StorageSetting(sinfo.getSettingJson());
		String rootpath = meta.BinPath;
		
		if(Storages.StorageType.DISK.name().equalsIgnoreCase(sinfo.getStorageType())){
			
			binaccessor = this.diskaccessor;
		}
		
		binaccessor.fillBinary(meta.BinaryId, storagesetting, rootpath, source);
	}
	
	/**
	 * dump certain chunk data of binary resource out to the chunk buffer
	 * 
	 * @param binaryKey the key of binary record
	 * @param chunkdata the chunk data holder
	 *   
	 **/
	public void dumpBinary(InfoId<Long> binaryKey, ContentRange range, OutputStream output)throws StorageException{
		BinaryInfo binfo = binarydao.query(binaryKey);
		String binaryURI = binfo.getStoreLocation();
		dumpBinary(binaryURI,range, output);
	}
	
	/**
	 * dump certain chunk data of binary resource out to the chunk buffer
	 * 
	 * @param binaryURI the uri string of source binary
	 * @param chunkdata the chunk data holder
	 *   
	 **/
	public void dumpBinary(String binaryURI, ContentRange range, OutputStream output)throws StorageException{
		
		BinUriMeta meta = new BinUriMeta(binaryURI);	
		BinaryAccessor binaccessor = null;
		
		StorageInfo sinfo = storagedao.query(meta.StorageId);
		StorageSetting storagesetting = new StorageSetting(sinfo.getSettingJson());
		String rootpath = meta.BinPath;
		
		if(Storages.StorageType.DISK.name().equalsIgnoreCase(sinfo.getStorageType())){
			
			binaccessor = this.diskaccessor;
		}

		binaccessor.dumpBinary(meta.BinaryId, storagesetting, rootpath, output);

	}
	
	/**
	 * dump the binary to the specified stream
	 * 
	 * @param binaryKey the key of binary record
	 * @param target the output stream
	 *   
	 **/
	public void dumpBinary(InfoId<Long> binaryKey, OutputStream target)throws StorageException{
		BinaryInfo binfo = binarydao.query(binaryKey);
		String binaryURI = binfo.getStoreLocation();
		dumpBinary(binaryURI, target);
	}
	
	/**
	 * dump the binary to the specified stream
	 * 
	 * @param binaryURI the uri string of binary
	 * @param target the output stream
	 *   
	 **/
	public void dumpBinary(String binaryURI, OutputStream target)throws StorageException{
		
		BinUriMeta meta = new BinUriMeta(binaryURI);
		BinaryAccessor binaccessor = null;
		StorageInfo sinfo = storagedao.query(meta.StorageId);
		StorageSetting storagesetting = new StorageSetting(sinfo.getSettingJson());
		String binpath = meta.BinPath;
		
		if(Storages.StorageType.DISK.name().equalsIgnoreCase(sinfo.getStorageType())){
			
			binaccessor = this.diskaccessor;
		}
		
		binaccessor.dumpBinary(meta.BinaryId, storagesetting, binpath, target);
	}
	
	/**
	 * this inner class parse the uri string into meta info of binary
	 *  
	 **/
	private static class BinUriMeta{
		
		/**
		 * constructor with uri string
		 **/
		public BinUriMeta(String uristr) throws StorageException{
			try {
				// wrap uri
				URI uri = new URI(uristr);
				int sid = uri.getPort();// id of storage
				this.StorageId = IdKey.STORAGE.getInfoId(sid);
				
				this.BinPath = uri.getPath();// path of binary
				int spos = this.BinPath.lastIndexOf('/');
				
				spos = this.BinPath.indexOf('.', spos);
				int epos = this.BinPath.lastIndexOf('.');
				
				String bidstr;
				if(spos == epos)
					bidstr = this.BinPath.substring(spos+1);
				else
					bidstr = this.BinPath.substring(spos+1, epos);
				// id of binary
				this.BinaryId = IdKey.BINARY.getInfoId(Long.valueOf(bidstr));
				
			} catch (URISyntaxException e) {
				LOGGER.error("Error fill binary", e);
				throw new StorageException("Error fill binary",e);
			}	
		}
		
		/**
		 * the storage id object
		 **/
		InfoId<Integer> StorageId;
		
		/**
		 * the binary id object
		 **/
		InfoId<Long> BinaryId;
		
		/**
		 * the binary path 
		 **/
		String BinPath;
	}
	
/*	public static void main(String[] args) throws StorageException{
		
		BinUriMeta bum = new BinUriMeta("gbin://storage:68/80/00/00/19.25.xlsx");
		System.out.println("port : " + bum.StorageId);
		BinUriMeta bum1 = new BinUriMeta("http://www.java2s.com:8080/80/00/00/19.25.xlsx");
		System.out.println("port : " + bum1.StorageId);
	}*/
}
