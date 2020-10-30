package test.baseapp.co.id.common.core.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import test.baseapp.co.id.common.stereotype.config.Config;
import test.baseapp.co.id.common.stereotype.config.repo.ConfigRepo;

public class ConfigService
{
	@SuppressWarnings("rawtypes")
	@Autowired
	private ConfigRepo configRepo;
	
	@SuppressWarnings("unchecked")
	@Cacheable("config")
	public String getConfig(String key)
	{
		return ((Optional<Config>) configRepo.findByKey(key))
				.map(Config::getValue)
				.orElse(null);
	}
}