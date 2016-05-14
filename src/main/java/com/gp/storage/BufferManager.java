package com.gp.storage;

import java.nio.ByteBuffer;
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

	public static int BUFFER_SIZE = 1* 1024 * 1024;	// size 1m

	private ByteBufferBuilder bufferbuilder = new ByteBufferBuilder(BUFFER_SIZE);
	
	private ByteBufferPool bufferpool = null;
	
	/**
	 * hidden default constructor 
	 **/
	private BufferManager(){
		
		bufferpool = new ByteBufferPool(bufferbuilder, 2, 6, 500l);
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
	public ChunkBuffer acquireChunkBuffer(long filesize, long offset, int chunkLength) throws StorageException{
		
		if(chunkLength > BUFFER_SIZE)
			throw new StorageException("chunk length greater than buffer capacity. ");
		
		ChunkBuffer chkbuffer = new ChunkBuffer(filesize, offset, chunkLength);

		ByteBuffer buffer = null;
		try {
			buffer = bufferpool.acquire();
			
			chkbuffer.setByteBuffer(buffer);
		} catch (PoolException e) {
			throw new StorageException("Error borrow buffer from pool", e);
		} catch (InterruptedException e) {
			throw new StorageException("Error borrow buffer from pool", e);
		}
		
		return chkbuffer;
	}
	
	/**
	 * borrow chunk buffer from buffer pool, the chunk buffer is auto closable.
	 *
	 * @param filesize the size of file
	 * @param chunksize the size of chunk
	 * @param chunkindex the index of chunk
	 * 
	 **/
	public ChunkBuffer acquireChunkBuffer(long filesize, int chunkindex) throws StorageException{

		long offset = ChunkBuffer.calculateOffset(filesize, chunkindex, BUFFER_SIZE);
		ChunkBuffer chkbuffer = new ChunkBuffer(filesize, offset, BUFFER_SIZE);

		ByteBuffer buffer = null;
		try {
			buffer = bufferpool.acquire();
			
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
	public void releaseChunkBuffer(ChunkBuffer buffer){
		
		bufferpool.release(buffer.getByteBuffer());
		// reset the byte buffer reference.
		buffer.setByteBuffer(null);
	}
	
}
