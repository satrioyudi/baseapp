package test.baseapp.co.id.common.stereotype.repo;

import org.springframework.data.repository.NoRepositoryBean;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import com.querydsl.core.types.Predicate;

@NoRepositoryBean
public interface EntityGraphOffsetLimitQuerydslPredicateExecutor<T> extends OffsetLimitQuerydslPredicateExecutor<T>, EntityGraphQuerydslPredicateExecutor<T>
{
	public Iterable<T> findAllOffsetLimit(Predicate predicate, OffsetLimit offsetLimitRequest, EntityGraph entityGraph);
}
