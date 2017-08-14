package com.gp.common;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gp.info.InfoId;

public class IdKeyTest {
	
	static Logger log = LoggerFactory.getLogger(IdKeyTest.class);
	
    public static void setup() {
        // loading log4j.xml file
        DOMConfigurator.configure("src/test/resources/log4j2.xml");
    }
    
	public static void main(String[] a){
		
		setup();
		InfoId<Long> id = IdKey.AUDIT.getInfoId(123l);
		
		String tracecode = IdKey.getTraceCode("E001", id);
		
		log.debug("trace code: {} / {}", id.toString(),tracecode);
		
		String node = IdKey.parseNodeCode(tracecode);
		InfoId<Long> nid = IdKey.parseInfoId(tracecode, Long.class);
		log.debug("parse code: {} / {}", node, nid);
	}
}
