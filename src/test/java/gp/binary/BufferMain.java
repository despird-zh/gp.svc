package gp.binary;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;

import com.gp.exception.StorageException;
import com.gp.storage.BufferManager;
import com.gp.storage.ChunkBuffer;
import com.gp.util.ByteUtils;
import com.gp.util.CommonUtils;

public class BufferMain {
	
	public static void main(String[] args) {
	    Scanner myScanner = new Scanner(System.in);
	    ExecutorService executor = Executors.newFixedThreadPool(10);
	    System.out.println("Welcome to my program that checks if a number is even or odd.");

	    while (true) {
	        System.out.println();
	        System.out.print("Please type number in a number ['q' to quit]: ");

	        String inText = myScanner.next();

	        if (inText.equals("q")){
	        	System.out.println("Now quit...");
	            break;
	        }
	        if (inText.equals("s")){
	   
	            continue;
	        }
	        
	        String[] numbers = StringUtils.split(inText, ',');
	        for(int i = 0; i < numbers.length; i++){
	        	int number = Integer.valueOf(numbers[i]);
	        	BufferThread task = new BufferThread(number);
	        	executor.execute(task);
	        }
	    }
	}
	
	public static class BufferThread implements Runnable{
		
		long filesize = 0;
		public BufferThread(long filesize){
			this.filesize = filesize;
		}
		@Override
		public void run() {
			
			int amt = ChunkBuffer.chunkAmount(filesize,  BufferManager.BUFFER_SIZE);
			try( ChunkBuffer buffer = BufferManager.instance().acquireChunkBuffer(filesize,  BufferManager.BUFFER_SIZE, 0)){
				
				for(int i = 0;i < buffer.bufferCapacity(); i++  )
					buffer.getByteBuffer().put((byte)1);
				
			} catch (StorageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 		
			
		}
		
	}
}
