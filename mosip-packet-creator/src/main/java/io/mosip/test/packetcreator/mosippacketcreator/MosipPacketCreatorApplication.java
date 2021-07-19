package io.mosip.test.packetcreator.mosippacketcreator;

import java.io.File;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;




@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})

public class MosipPacketCreatorApplication {

	  private static ConfigurableApplicationContext context;
	
	public static void main(String[] args) {
		System.setProperty("spring.devtools.restart.enabled", "false");
		//		#security.ignored=/**
	//	System.setProperty("security.basic.enabled", "false");
	//	System.setProperty("management.security.enabled", "false");
	//	System.setProperty("security.ignored", "/**");
	
	
		context = SpringApplication.run(MosipPacketCreatorApplication.class, args);
	}
	public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(MosipPacketCreatorApplication.class, args.getSourceArgs());
        });
        thread.setDaemon(false);
        thread.start();
    }
	/*
	@Bean
    public SecurityWebFilterChain chain(ServerHttpSecurity http, AuthenticationWebFilter webFilter) {
        return http.authorizeExchange().anyExchange().permitAll().and()
                .csrf().disable()
                .build();
	}*/
	@Bean
	@ConditionalOnProperty(name = "spring.config.location", matchIfMissing = false)
	public PropertiesConfiguration propertiesConfiguration(
	  @Value("${spring.config.location}") String path) throws Exception {
	    String filePath = new File(path.substring("file:".length())).getCanonicalPath();
	    
	    PropertiesConfiguration configuration = new PropertiesConfiguration(
	      new File(filePath));
	    configuration.setReloadingStrategy(new FileChangedReloadingStrategy());
	    return configuration;
	}
	
}
