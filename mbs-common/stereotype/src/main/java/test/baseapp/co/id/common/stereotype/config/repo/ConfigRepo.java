package test.baseapp.co.id.common.stereotype.config.repo;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import test.baseapp.co.id.common.stereotype.config.Config;

public interface ConfigRepo<C extends Config, ID extends Serializable> extends CrudRepository<C, ID>
{
	public Optional<C> findByKey(String key);
}