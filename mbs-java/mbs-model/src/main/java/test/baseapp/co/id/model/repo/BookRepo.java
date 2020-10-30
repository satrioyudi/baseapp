package test.baseapp.co.id.model.repo;

import java.math.BigInteger;

import org.springframework.data.repository.CrudRepository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphCrudRepository;

import test.baseapp.co.id.common.stereotype.repo.EntityGraphOffsetLimitQuerydslPredicateExecutor;
import test.baseapp.co.id.model.entity.Book;

public interface BookRepo extends CrudRepository<Book, BigInteger>, EntityGraphCrudRepository<Book, BigInteger>, EntityGraphOffsetLimitQuerydslPredicateExecutor<Book>{

}
