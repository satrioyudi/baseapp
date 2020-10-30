package test.baseapp.co.id.common.core.autoconfigurer;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.github.benmanes.caffeine.cache.Caffeine;

import test.baseapp.co.id.common.core.service.ConfigService;

@Configuration
@ConditionalOnProperty(prefix = "config", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableCaching
@EnableScheduling
public class ConfigAutoConfiguration
{
	@Bean
	@ConditionalOnMissingBean
	public ConfigService configService()
	{
		return new ConfigService();
	}
	
	@Bean
	public Cache configCache(@Value("${config.cacheAge:1800000}") Long timeout)
	{
		return new CaffeineCache("config", Caffeine.newBuilder()
				.expireAfterWrite(timeout, TimeUnit.MILLISECONDS)
				.build());
	}
}