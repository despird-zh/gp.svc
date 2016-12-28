package com.gp.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.gp.common.Storages;
import com.gp.dao.BinaryDAO;
import com.gp.dao.StorageDAO;
import com.gp.exception.StorageException;
import com.gp.info.InfoId;

/**
 * the accessor to read/write binary on disk. the file operation utilize the guava library.
 * 
 * @author gary diao
 * @version 0.1 2015-12-12
 * 
 **/
public class DiskBinaryAccessor extends BinaryAccessor{

	static Logger LOGGER = LoggerFactory.getLogger(DiskBinaryAccessor.class);
		
	public DiskBinaryAccessor(StorageDAO storagedao, BinaryDAO binarydao) {
		super(storagedao, binarydao);
	}

	static int BUFFER_SIZE = 4 * 1024;
	
	public void dumpBinary(InfoId<Long> binaryId, StorageSetting setting, String path, ContentRange range, OutputStream target) throws StorageException{
		
		String rootpath = setting.getValue(Storages.StoreSetting.StorePath.name());
		
		byte[] data = new byte[BUFFER_SIZE];
		try(RandomAccessFile srcbinary = new RandomAccessFile(rootpath + path,"r")){
			
			long begin = range.getStartPos();
			srcbinary.seek(begin);
			int remain = range.getRangeLength();
			int count = 0;
			while (remain > 0 && count != -1) {
				
				count = remain > BUFFER_SIZE ? 
						srcbinary.read(data):
						srcbinary.read(data, 0, remain);
						
				target.write(data, 0, count);
				begin += count;
				remain -= count;
				srcbinary.seek(begin);
			}
			
		}catch( IOException ioe){
			throw new StorageException("Fail to dump binary to output stream", ioe);
		}
		
	}

	public void dumpBinary(InfoId<Long> binaryId, StorageSetting setting, String path, OutputStream target)throws StorageException {
		
		String rootpath = setting.getValue(Storages.StoreSetting.StorePath.name());
		
		File srcbinary = new File(rootpath + path);

		try {
			Files.copy(srcbinary, target);
		} catch (IOException e) {
			throw new StorageException("fail to copy the source binary to target.",e);
		}
	}
	
	public void fillBinary(InfoId<Long> binaryId, StorageSetting setting, String path, ContentRange range, InputStream source) throws StorageException{
		
		String rootpath = setting.getValue(Storages.StoreSetting.StorePath.name());
		String storelocation = rootpath + path;
		File tgtfile = new File(storelocation);
		if(!tgtfile.getParentFile().exists()){
			
			boolean success = tgtfile.getParentFile().mkdirs();
			if(!success)
				throw new StorageException("fail prepare store location : {}", storelocation);
		}
		
		byte[] data = new byte[BUFFER_SIZE];
		try(RandomAccessFile tgtbinary = new RandomAccessFile(rootpath + path,"w")){
			
			long begin = range.getStartPos();
			tgtbinary.seek(begin);
			
			int remain = range.getRangeLength();
			int count = 0;
			
			while (remain > 0 && count != -1) {
				
				count = remain > BUFFER_SIZE ? 
						source.read(data):
						source.read(data, 0, remain);
						
				tgtbinary.write(data, 0, count);
				begin += count;
				remain -= count;
				tgtbinary.seek(begin);
			}
		}catch( IOException ioe){
			throw new StorageException("Fail to fill binary to target stream", ioe);
		}
	}
	
	public void fillBinary(InfoId<Long> binaryId, StorageSetting setting, String path, final InputStream source) throws StorageException{
		
		String rootpath = setting.getValue(Storages.StoreSetting.StorePath.name());
		
		File tgtbinary = new File(rootpath + path);
		
		if(!tgtbinary.getParentFile().exists()){
			
			tgtbinary.getParentFile().mkdirs();
		}
		
		try {
			
			ByteSource bsource = new ByteSource() {
			      @Override
			      public InputStream openStream() throws IOException {
			        return source;
			      }
			    };
			bsource.copyTo(new FileOutputStream(tgtbinary));
			
		} catch (IOException e) {
			throw new StorageException("fail to copy the source binary to target.",e);
		}
	}

}
