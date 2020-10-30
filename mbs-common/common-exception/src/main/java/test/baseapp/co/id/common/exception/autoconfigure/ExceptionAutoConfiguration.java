package test.baseapp.co.id.common.exception.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import test.baseapp.co.id.common.exception.service.ExceptionService;

@Configuration
@ConditionalOnProperty(prefix = "exception", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableCaching
@EnableScheduling
@ComponentScan(basePackages = {"com.fusi24.common.exception.controller", "com.fusi24.common.exception.service"})
public class ExceptionAutoConfiguration {
	
//	@Bean(name = "com.fusi24.common.exception.service.ExceptionService")
//	@ConditionalOnMissingBean
//	public ExceptionService exceptionService() {
//		return new ExceptionService();
//	}

}
