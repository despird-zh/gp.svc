package com.gp.storage;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gp.exception.PoolException;
import com.gp.exception.StorageException;
import com.gp.pool.ByteBufferBuilder;
import com.gp.pool.ByteBufferPool;

/**
 * BufferManager hold the buffer pool instance to allocate pooled buffer and re-cache it.
 * 
 * @author gary diao
 * @version 0.1 2015-12-12
 * 
 **/
public class BufferManager {

	static Logger LOGGER = LoggerFactory.getLogger(BufferManager.class);
	
	public static final int SIZE_128K = 128 * 1024;	
	public static final int SIZE_512K = 512 * 1024;	
	public static final int SIZE_1M = 1 * 1024 * 1024;	
	public static final int SIZE_2M = 2 * SIZE_1M;	
	public static final int SIZE_4M = 2 * SIZE_2M;
	
	public static final int DEFAULT_CHUNKS = 10;
	
	private ByteBufferBuilder bufferbuilder = new ByteBufferBuilder(SIZE_128K);
	private ByteBufferPool bufferpool = null;
	
	/** default pool setting {buffer size, min idle, max idle}*/
	private int[][] bufferSettingArray = new int[][]{
		{SIZE_128K,2,4},
		{SIZE_512K,2,4},
		{SIZE_1M,1,4},
		{SIZE_2M,1,2},
		{SIZE_4M,1,2}};
		
	/**
	 * hidden default constructor 
	 **/
	private BufferManager(){
		
		bufferpool = new ByteBufferPool(bufferbuilder, bufferSettingArray, 500l);
	}
	
	/**
	 * hidden instance
	 **/
	private static BufferManager instance;
	
	static{
		
		instance();		
	}
	
	/**
	 * get singleton instance 
	 **/
	public static BufferManager instance(){
		
		if(null == instance) 
			instance = new BufferManager();
		
		return instance;
	}
	
	/**
	 * borrow chunk buffer from buffer pool, the chunk buffer is auto closable.
	 *
	 * @param filesize the size of file
	 * @param chunksize the size of chunk
	 * @param chunkindex the index of chunk
	 * 
	 **/
	public ChunkBuffer borrowChunkBuffer(long filesize, int chunksize, int chunkindex) throws StorageException{

		ChunkBuffer chkbuffer = new ChunkBuffer(filesize, chunksize);
		chkbuffer.setChunkIndex(chunkindex);
		// find nearest size buffer
		int buffersize = nearestBufferSize((int)chkbuffer.chunkLength());
		
		ByteBuffer buffer = null;
		try {
			buffer = bufferpool.borrowItem(buffersize);
			
			chkbuffer.setByteBuffer(buffer);
		} catch (PoolException e) {
			throw new StorageException("Error borrow buffer from pool", e);
		} catch (InterruptedException e) {
			throw new StorageException("Error borrow buffer from pool", e);
		}
		
		return chkbuffer;
	}
	
	/**
	 * return the chunk buffer to buffer pool
	 * 
	 * @param buffer the chunk buffer.
	 * 
	 **/
	public void returnChunkBuffer(ChunkBuffer buffer){
		
		bufferpool.returnItem(buffer.getByteBuffer());
		// reset the byte buffer
		buffer.setByteBuffer(null);
	}
	
	/**
	 * find a nearest buffer size
	 * 
	 * @param actuallength the length of actual data length 
	 * 
	 **/
	public int nearestBufferSize(int actuallength){		
		int[] sizearray = bufferpool.getBufferSizes();
		
		Arrays.sort(sizearray);
		
		for(int i = 0; i < sizearray.length; i++){
			
			if(actuallength <= sizearray[i])
				return sizearray[i];
		}
		
		return sizearray[sizearray.length-1];
		
	}
	
	/**
	 * find a recommend buffer size as per the file size 
	 * 
	 * @param filesize the size of file
	 **/
	public int recommendBufferSize(long filesize){
		
		int chunksize = (int)(filesize / DEFAULT_CHUNKS);		
		int[] sizearray = bufferpool.getBufferSizes();
		
		Arrays.sort(sizearray);
		
		for(int i = 0; i < sizearray.length; i++){
			
			if(chunksize <= sizearray[i])
				return sizearray[i];
		}
		
		return sizearray[sizearray.length-1];
	}
	
	/**
	 * Get the available buffer size array 
	 **/
	public int[] getBufferSizeArray(){
		
		return bufferpool.getBufferSizes();
	}
	
	/**
	 * get the statistics of the buffer pool<br>
	 * <pre>
	 * {
	 *    {[buffer size], [cache size]}
	 *    ...
	 * } 
	 * </pre>
	 **/
	public int[][] getPoolStatistics(){
		
		return bufferpool.getStatistics();
	}
}
