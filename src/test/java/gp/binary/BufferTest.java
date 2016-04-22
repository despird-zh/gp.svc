package gp.binary;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.gp.info.InfoId;
import com.gp.storage.BinaryManager;
import com.gp.storage.BufferManager;
import com.gp.util.ByteUtils;
import com.gp.util.CommonUtils;
import com.gp.util.StorageUtils;

@ContextConfiguration(locations = "/mysql-test.xml")
public class BufferTest extends AbstractJUnit4SpringContextTests{
	
	static Logger LOGGER = LoggerFactory.getLogger(BufferTest.class);
	
	@Test
	public void test() throws Exception{
				
		BufferManager inst = BufferManager.instance();
		int recsize;
		int filesize;
		filesize = 1024 * 129;
		recsize = inst.recommendBufferSize(filesize);
		LOGGER.debug("input : {} -> recommend : {}", CommonUtils.humanReadableByteCount(filesize, true), 
				CommonUtils.humanReadableByteCount(recsize, false));
		
		filesize = 1024 * 512 * 10 +1;
		recsize = inst.recommendBufferSize(filesize);
		LOGGER.debug("input : {} -> recommend : {}", CommonUtils.humanReadableByteCount(filesize, true), 
				CommonUtils.humanReadableByteCount(recsize, false));
		
		filesize = 1024 * 1024 * 10 +1;
		recsize = inst.recommendBufferSize(filesize);
		LOGGER.debug("input : {} -> recommend : {}", CommonUtils.humanReadableByteCount(filesize, true), 
				CommonUtils.humanReadableByteCount(recsize, false));
		
		filesize = 1024 * 1024 * 20 +1;
		recsize = inst.recommendBufferSize(filesize);
		LOGGER.debug("input : {} -> recommend : {}", CommonUtils.humanReadableByteCount(filesize, true), 
				CommonUtils.humanReadableByteCount(recsize, false));
		
		filesize = 1024 * 1024 * 40 +1;
		recsize = inst.recommendBufferSize(filesize);
		LOGGER.debug("input : {} -> recommend : {}", CommonUtils.humanReadableByteCount(filesize, true), 
				CommonUtils.humanReadableByteCount(recsize, false));
				
	}

}
