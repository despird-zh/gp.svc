package com.gp.storage;

import java.nio.ByteBuffer;

/**
 * this class implements AutoCloseable interface
 * int close() method it return self to BufferManager.
 * 
 * @author gary diao
 * @version 0.1 2015-12-12
 * 
 **/
public class ChunkBuffer implements AutoCloseable{

	/**
	 * constructor with file size and chunk size 
	 **/
	public ChunkBuffer(Long fileSize, Long chunkOffset, int chunkLength){
		
		this.fileSize = fileSize;
		this.chunkOffset = chunkOffset;
		this.chunkLength = chunkLength;
	}
	
	private long fileSize = -1l; // size of file	
	private long chunkOffset = 0l; // index of expected chunk
	private ByteBuffer chunkData = null; // data
	private long chunkLength = -1; // length of expected chunk data
	
	/**
	 * get the offset of current chunk 
	 **/
	public long getChunkOffset(){
		
		return chunkOffset;
	}
	
	/**
	 * get the bytes array length of current chunk 
	 **/
	public long getChunkLength(){
		
		return chunkLength;
	}
	
	/**
	 * set the index of chunk, this trigger re-calculation 
	 **/
	public void setChunkOffset(long chunkOffset){
		this.chunkOffset = chunkOffset;
	}
	
	/**
	 * set the byte buffer
	 **/
	public void setByteBuffer(ByteBuffer chunkData){
		
		this.chunkData = chunkData;
	}
	
	/**
	 * get byte buffer 
	 **/
	public ByteBuffer getByteBuffer(){
		
		return this.chunkData;
	}

	/**
	 *  
	 **/
	@Override
	public void close() {
		
		BufferManager.instance().releaseChunkBuffer(this);
	}
	
	/**
	 * return the capacity of buffer 
	 **/
	public int bufferCapacity(){
		
		return chunkData.capacity();
	}
	
	/**
	 * the static method to calculate the chunk count as per specified chunk size
	 * 
	 * @param filesize 
	 * @param chunkSize
	 * @return the amount of chunks
	 **/
	public static int calculateAmount(long filesize, int chunkSize){
		// re-calculate the chunks amount
		int chunkAmount = (int) (filesize / chunkSize);
		if (filesize % chunkSize > 0) {
			chunkAmount++;
		}
		return chunkAmount;
	}
	
	/**
	 * static method to calculate the offset position 
	 * @param fileSize the size of file
	 * @param chunkIndex the index of file chunks
	 * @param chunkSize the size of file chunk
	 *  
	 **/
	public static long calculateOffset(Long fileSize, int chunkIndex, int chunkSize){
		// index set 
		if(chunkIndex >= 0){
			long bytesRemaining = fileSize - chunkIndex * chunkSize;
			// re-calculate the current chunk length
			if(bytesRemaining <= 0 ) {
				return 0l;
				
			}else{			
				return (bytesRemaining > (long)chunkSize) ? chunkSize : bytesRemaining;
			}
		}
		
		return -1;
	}
	
	/**
	 * static method to calculate the offset position 
	 * @param fileSize the size of file
	 * @param chunkIndex the index of file chunks
	 * @param chunkSize the size of file chunk
	 *  
	 **/
	public static long calculateLength(Long fileSize, int chunkIndex, int chunkSize){
		// index set 
		if(chunkIndex > 0){
			long bytesRemaining = fileSize - chunkIndex * chunkSize;
			// re-calculate the current chunk length
			if(bytesRemaining <= 0 ) {
				return fileSize - (chunkIndex - 1) * chunkSize;
				
			}else{			
				return bytesRemaining;
			}
		}else{
			return Math.min(fileSize, chunkSize);
		}

	}
}
