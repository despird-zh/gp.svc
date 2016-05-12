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
	public ChunkBuffer(Long fileSize, int chunkSize){
		
		this.fileSize = fileSize;
		this.chunkSize = chunkSize;
		calculate();
	}
	
	private long fileSize = -1l; // size of file	
	private long chunkAmount = -1l; // amount of total chunks
	private int chunkIndex = 0; // index of expected chunk
	private int chunkSize = -1; // average size of chunk
	private ByteBuffer chunkData = null; // data
	private int chunkLength = -1; // length of expected chunk data
	
	/**
	 * get the offset of current chunk 
	 **/
	public long chunkOffset(){
		
		return chunkIndex * chunkSize;
	}
	
	/**
	 * get the bytes array length of current chunk 
	 **/
	public long chunkLength(){
		
		return chunkLength;
	}
	
	/**
	 * set the index of chunk, this trigger re-calculation 
	 **/
	public void setChunkIndex(int index){
		this.chunkIndex = index;
		calculate();
	}
	
	/**
	 * get the count of chunks  
	 **/
	public long chunkAmount(){
		
		return chunkAmount;
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
	
	private void calculate(){
		// index set 
		if(chunkIndex >= 0){
			long bytesRemaining = fileSize - chunkIndex * chunkSize;
			// re-calculate the current chunk length
			if(bytesRemaining <= 0 ) {
				this.chunkLength = 0;
				
			}else{			
				this.chunkLength = (bytesRemaining > (long)chunkSize) ? 
						chunkSize : (int)bytesRemaining;
			}
		}
		// re-calculate the chunks amount
		chunkAmount = (int) (fileSize / chunkSize);
		if (fileSize % chunkSize > 0) {
			chunkAmount++;
		}
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
	public static int chunkAmount(long filesize, int chunkSize){
		// re-calculate the chunks amount
		int chunkAmount = (int) (filesize / chunkSize);
		if (filesize % chunkSize > 0) {
			chunkAmount++;
		}
		return chunkAmount;
	}
}
