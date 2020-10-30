package test.baseapp.co.id.common.stereotype.repo;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.querydsl.core.types.Predicate;

@NoRepositoryBean
public interface OffsetLimitQuerydslPredicateExecutor<T> extends QuerydslPredicateExecutor<T>
{
	public Iterable<T> findAllOffsetLimit(Predicate predicate, OffsetLimit offsetLimitRequest);
}
