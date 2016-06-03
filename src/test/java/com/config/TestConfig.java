package com.config;

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

}
