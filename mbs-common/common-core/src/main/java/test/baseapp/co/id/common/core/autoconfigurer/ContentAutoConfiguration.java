package test.baseapp.co.id.common.core.autoconfigurer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import test.baseapp.co.id.common.core.service.ContentService;


@Configuration
@ConditionalOnProperty(prefix = "content", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableCaching
@EnableScheduling
public class ContentAutoConfiguration
{
	@Bean(name = "com.fusi24.common.core.service.ContentService")
	@ConditionalOnMissingBean
	public ContentService contentService()
	{
		return new ContentService();
	}
}