package gp.binary;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import com.gp.exception.StorageException;
import com.gp.storage.BufferManager;
import com.gp.storage.ChunkBuffer;
import com.gp.storage.ContentRange;
import com.gp.util.BufferInputStream;
import com.gp.util.BufferOutputStream;

public class StreamBufferTest {

	public static void main(String[] args) throws Exception{

		StreamBufferTest stest = new StreamBufferTest();
		stest.testCopyWhole();
		stest.testCopyChunk();
	}
	
	public void testCopyChunk() throws IOException, StorageException{
		String src = "/jdev/blogs.pdf";
		String dst = "/jdev/blogs-new.pdf";
		
		File srcfile = new File(src);
		int chunkamt = ChunkBuffer.calculateAmount(srcfile.length(), BufferManager.BUFFER_SIZE);
		
		RandomAccessFile raf = new RandomAccessFile(src, "r");
        long numSplits = chunkamt; //from user input, extract it from args

        for(int destIx=0; destIx < numSplits; destIx++) {
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(src+"."+destIx));
            long numBytes = ChunkBuffer.calculateLength(srcfile.length(), destIx, BufferManager.BUFFER_SIZE);
            
            readWrite( raf,  bw,  numBytes);
            bw.close();
        }
        
        raf.close();
        
        FileOutputStream fos = new FileOutputStream(new File(dst));
        for(int destIx=0; destIx < numSplits; destIx++) {
            InputStream in = new FileInputStream(new File(src+"."+destIx));
            
            testCopyChunk(srcfile.length(), destIx, fos, in);
            in.close();
        }
        fos.close();
	}
	
	public void testCopyChunk(long filesize, int destIx, FileOutputStream fos, InputStream in) throws StorageException, IOException{
		long numBytes = ChunkBuffer.calculateLength(filesize, destIx, BufferManager.BUFFER_SIZE);
       
		ChunkBuffer cbuffer = BufferManager.instance().acquireChunkBuffer(filesize, destIx);
		BufferOutputStream bos = new BufferOutputStream(cbuffer.getByteBuffer());
		bos.writeFromStream(in);
		bos.close();
		cbuffer.getByteBuffer().flip();
		System.out.println("chunk offset : " + cbuffer.getChunkOffset());
		// skip offset length 
		FileChannel ch = fos.getChannel();
		ch.position(cbuffer.getChunkOffset());
		int dlen = ch.write(cbuffer.getByteBuffer());
		System.out.println("chunk["+destIx+"]'s len : " + dlen);
		cbuffer.close();
	}
	
	// the file must be less than BufferManager.BUFFER_SIZE; 1 MiB
	public void testCopyWhole() throws StorageException, IOException{
		String src = "/jdev/tomp2p.pdf";
		String dst = "/jdev/topp2p-new.pdf";
		
		File srcfile = new File(src);
		ContentRange range = new ContentRange();
		range.setFileSize(srcfile.length());
		range.setStartPos(0l);
		range.setEndPos(srcfile.length() -1);
		InputStream in = new FileInputStream(srcfile);
		OutputStream out = new FileOutputStream(new File(dst));
		
		testCopyWhole(in, out, range);
	}
	
	public void testCopyWhole(InputStream in, OutputStream out, ContentRange range) throws StorageException, IOException{
		
		ChunkBuffer cbuffer = BufferManager.instance().acquireChunkBuffer(range.getFileSize(), 0l, range.getRangeLength());
		BufferOutputStream bos = new BufferOutputStream(cbuffer.getByteBuffer());
		bos.writeFromStream(in);
		bos.close();
		cbuffer.getByteBuffer().flip();
		BufferInputStream bis = new BufferInputStream(cbuffer.getByteBuffer());
		bis.readToStream(out);
		bis.close();
		cbuffer.close();
	}
	
	static void readWrite(RandomAccessFile raf, BufferedOutputStream bw, long numBytes) throws IOException {
        byte[] buf = new byte[(int) numBytes];
        int val = raf.read(buf);
        if(val != -1) {
            bw.write(buf);
        }
    }
}
