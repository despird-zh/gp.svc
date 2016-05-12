package gp.binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.gp.common.IdKey;
import com.gp.exception.StorageException;
import com.gp.info.InfoId;
import com.gp.storage.BinaryManager;
import com.gp.storage.BufferManager;
import com.gp.storage.ChunkBuffer;
import com.gp.util.BufferInputStream;
import com.gp.util.BufferOutputStream;
import com.gp.util.ByteUtils;
import com.gp.util.CommonUtils;
import com.gp.util.StorageUtils;

@ContextConfiguration(locations = "/mysql-test.xml")
public class BinaryTest extends AbstractJUnit4SpringContextTests{
	
	public void test() throws Exception{
		
		String bpath = StorageUtils.toURIStr(IdKey.STORAGE.getInfoId(3), 21l,null, "txt");
		
		File tgt = new File("d:\\temp.txt");
		
		FileOutputStream fo = new FileOutputStream(tgt);
		
		BinaryManager.instance().dumpBinary(bpath, fo);
	}

	public void testDumpInChunk() throws StorageException, IOException{
		
		File f = new File("D:\\n.repo\\80\\00\\00\\16.22.zip");
		File f1 = new File("D:\\16.22.zip");
		long filesize = f.length();
		String bpath = StorageUtils.toURIStr(IdKey.STORAGE.getInfoId(3), 22l,null, "zip");
		System.out.println("bpath : " + bpath);

		FileOutputStream fo = new FileOutputStream(f1);
		int chunks = ChunkBuffer.chunkAmount(filesize, BufferManager.BUFFER_SIZE);
		
		InfoId<Long> bid = IdKey.BINARY.getInfoId(22l);
		for(int i = 0 ; i < chunks; i++){
			
			try(ChunkBuffer cbuffer = BufferManager.instance().acquireChunkBuffer(filesize, BufferManager.BUFFER_SIZE, i)){
				BinaryManager.instance().dumpBinaryChunk(bid, cbuffer);
				System.out.println("limit : " + cbuffer.getByteBuffer().limit() +"/pos : " + cbuffer.getByteBuffer().position());
				
				BufferInputStream bis = new BufferInputStream(cbuffer.getByteBuffer());
				int count = bis.readToStream(fo);
				System.out.println("count : " + count);
			}
		}
		fo.flush();
		fo.close();
	}
	
	@Test
	public void testFillInChunk() throws StorageException, IOException{
		
		File f1 = new File("D:\\16.22.zip");
		long filesize = f1.length();

		FileInputStream fi = new FileInputStream(f1);
		int chunks = ChunkBuffer.chunkAmount(filesize, BufferManager.BUFFER_SIZE);
		
		InfoId<Long> bid = IdKey.BINARY.getInfoId(1122l);
		for(int i = 0 ; i < chunks; i++){
			
			try(ChunkBuffer cbuffer = BufferManager.instance().acquireChunkBuffer(filesize, BufferManager.BUFFER_SIZE, i)){
				
				BufferOutputStream bos = new BufferOutputStream(cbuffer.getByteBuffer());
				long count = bos.writeFromStream(fi);
				System.out.println("limit : " + cbuffer.getByteBuffer().limit() +"/pos : " + cbuffer.getByteBuffer().position());
				System.out.println("count : " + count);
				BinaryManager.instance().fillBinaryChunk(bid, cbuffer);
			}
		}
	}
}
