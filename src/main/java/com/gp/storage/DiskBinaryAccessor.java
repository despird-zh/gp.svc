package com.gp.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.gp.common.Storages;
import com.gp.dao.BinaryDAO;
import com.gp.dao.StorageDAO;
import com.gp.exception.StorageException;
import com.gp.info.InfoId;
import com.gp.util.BufferOutputStream;

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

	public void fillBinaryChunk(InfoId<Long> binaryId, StorageSetting setting, String path, ChunkBuffer chunkdata) throws StorageException{
		
		String rootpath = setting.getValue(Storages.StoreSetting.StorePath.name());
		String storelocation = rootpath + path;
		File tgtbinary = new File(storelocation);
		if(!tgtbinary.getParentFile().exists()){
			
			boolean success = tgtbinary.getParentFile().mkdirs();
			if(!success)
				throw new StorageException("fail prepare store location : {}", storelocation);
		}
		
		try (FileChannel ch = FileChannel.open(Paths.get(rootpath + path), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND)){
		   
		    int dlen = ch.write(chunkdata.getByteBuffer(),chunkdata.getChunkOffset());
		    LOGGER.debug("-- write pos : {} / len : {} to storeage : {}",chunkdata.getChunkOffset(), dlen, storelocation);
		    ch.force(true);
		} catch (IOException e) {
			throw new StorageException("fail to copy the source binary to target.",e);
		}
	}

	public void dumpBinaryChunk(InfoId<Long> binaryId, StorageSetting setting, String path, ChunkBuffer chunkdata) throws StorageException{
		
		String rootpath = setting.getValue(Storages.StoreSetting.StorePath.name());
		
		File srcbinary = new File(rootpath + path);
		try(BufferOutputStream outputstream = new BufferOutputStream(chunkdata.getByteBuffer());
			FileInputStream fis = new FileInputStream(srcbinary);
			) {
			// skip offset length 
			fis.skip(chunkdata.getChunkOffset());
			// copy from file
			outputstream.writeFromStream(fis);
			
		} catch (IOException e) {
			throw new StorageException("fail to copy the source binary to target.",e);
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

	public void dumpBinary(InfoId<Long> binaryId, StorageSetting setting, String path, OutputStream target)throws StorageException {
		
		String rootpath = setting.getValue(Storages.StoreSetting.StorePath.name());
		
		File srcbinary = new File(rootpath + path);

		try {
			Files.copy(srcbinary, target);
		} catch (IOException e) {
			throw new StorageException("fail to copy the source binary to target.",e);
		}
	}
}
