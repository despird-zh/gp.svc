package com.config;

import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import com.gp.config.ServiceConfigurer;

@Configuration
@Import({ ServiceConfigurer.class })
@ImportResource({
     "classpath:/mysql-test.xml"
})
public class TestConfig {

	static{
        // loading log4j.xml file
        DOMConfigurator.configure("src/test/resources/log4j2.xml");
    }
}
