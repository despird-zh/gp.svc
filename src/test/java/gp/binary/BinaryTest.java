package gp.binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.config.TestConfig;
import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.exception.StorageException;
import com.gp.info.InfoId;
import com.gp.storage.BinaryManager;
import com.gp.util.StorageUtils;

@ContextConfiguration(classes={TestConfig.class})
public class BinaryTest extends AbstractJUnit4SpringContextTests{
	
	public void test() throws Exception{
		
		String bpath = StorageUtils.toURIStr(IdKeys.getInfoId(IdKey.GP_STORAGES,3), 21l, "txt");
		
		File tgt = new File("d:\\temp.txt");
		
		FileOutputStream fo = new FileOutputStream(tgt);
		
		BinaryManager.instance().dumpBinary(bpath, fo);
	}

	public void testDumpInChunk() throws StorageException, IOException{
		
		File f = new File("D:\\n.repo\\80\\00\\00\\16.22.zip");
		File f1 = new File("D:\\16.22.zip");
		long filesize = f.length();
		String bpath = StorageUtils.toURIStr(IdKeys.getInfoId(IdKey.GP_STORAGES,3), 22l,"zip");
		System.out.println("bpath : " + bpath);

		FileOutputStream fo = new FileOutputStream(f1);
		int chunks = StorageUtils.calcAmount(filesize, 2*1024*1024);
		
		InfoId<Long> bid = IdKeys.getInfoId(IdKey.GP_BINARIES,22l);
		for(int i = 0 ; i < chunks; i++){
			
			//BinaryManager.instance().dumpBinaryChunk(bid, cbuffer);
			
		}
		fo.flush();
		fo.close();
	}
	
	@Test
	public void testFillInChunk() throws StorageException, IOException{
		
		File f1 = new File("D:\\16.22.zip");
		long filesize = f1.length();

		FileInputStream fi = new FileInputStream(f1);
		int chunks = StorageUtils.calcAmount(filesize, 2*1024*1024);
		
		InfoId<Long> bid = IdKeys.getInfoId(IdKey.GP_BINARIES,1122l);
		for(int i = 0 ; i < chunks; i++){
			//BinaryManager.instance().fillBinaryChunk(bid, cbuffer);
			
		}
	}
}
