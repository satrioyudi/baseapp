package test.baseapp.co.id.common.stereotype.security.repo;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import test.baseapp.co.id.common.stereotype.security.User;

public interface UserRepo<U extends User, ID extends Serializable> extends CrudRepository<U, ID>
{
	public Optional<U> findOneByUsernameIgnoreCase(String username);
}