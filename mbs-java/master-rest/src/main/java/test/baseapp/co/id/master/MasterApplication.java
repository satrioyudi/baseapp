package test.baseapp.co.id.master;

import javax.annotation.PostConstruct;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import test.baseapp.co.id.common.rest.server.RestControllerEngine;
import test.baseapp.co.id.common.stereotype.repo.ExtendedEntityGraphQuerydslRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "test.baseapp.co.id.model.repo", repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class, repositoryBaseClass = ExtendedEntityGraphQuerydslRepository.class)
@EntityScan(basePackages = {"test.baseapp.co.id.model.entity"})
public class MasterApplication {
	public static void main(String[] args) {
		SpringApplication.run(MasterApplication.class, args);
	}
	
	@Bean
	public RestControllerEngine restControllerEngine() {
		return new RestControllerEngine();
	}
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@PostConstruct
	public void init()
	{
		objectMapper.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
	}
	
	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {
	    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
	    factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
	        @Override
	        public void customize(Connector connector) {
	            connector.setProperty("relaxedQueryChars", "|{}[]");
	        }
	    });
	    return factory;
	}

}
