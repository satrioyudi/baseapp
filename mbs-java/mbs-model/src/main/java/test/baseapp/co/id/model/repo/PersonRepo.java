package test.baseapp.co.id.model.repo;

import java.math.BigInteger;
import org.springframework.data.repository.CrudRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphCrudRepository;
import com.google.common.base.Optional;

import java.util.List;
import test.baseapp.co.id.common.stereotype.repo.EntityGraphOffsetLimitQuerydslPredicateExecutor;
import test.baseapp.co.id.model.entity.Person;

public interface PersonRepo extends CrudRepository<Person, BigInteger>, EntityGraphCrudRepository<Person, BigInteger>, EntityGraphOffsetLimitQuerydslPredicateExecutor<Person>{
	Optional<List<Person>> findAllById(BigInteger id);
}
