package test.baseapp.co.id.common.stereotype.repo;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphQuerydslRepository;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.AbstractJPAQuery;

public class ExtendedEntityGraphQuerydslRepository<T, ID extends Serializable> extends EntityGraphQuerydslRepository<T, ID> 
		implements OffsetLimitQuerydslPredicateExecutor<T>, EntityGraphOffsetLimitQuerydslPredicateExecutor<T>
{
	private final EntityPath<T> path;
	private final Querydsl querydsl;
	
	public ExtendedEntityGraphQuerydslRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager)
	{
		super(entityInformation, entityManager);
		
		EntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;
		
		this.path = resolver.createPath(entityInformation.getJavaType());
		this.querydsl = new Querydsl(entityManager, new PathBuilder<T>(path.getType(), path.getMetadata()));
	}

	public ExtendedEntityGraphQuerydslRepository(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager, EntityPathResolver resolver)
	{
		super(entityInformation, entityManager, resolver);
		
		this.path = resolver.createPath(entityInformation.getJavaType());
		this.querydsl = new Querydsl(entityManager, new PathBuilder<T>(path.getType(), path.getMetadata()));
	}

	@Override
	public Iterable<T> findAllOffsetLimit(Predicate predicate, OffsetLimit offsetLimitRequest)
	{
		JPQLQuery<T> query = querydsl.applyPagination(offsetLimitRequest, createQuery(predicate).select(path));
		
		return query.select(path).fetch();
	}
	
	@Override
	public Iterable<T> findAllOffsetLimit(Predicate predicate, OffsetLimit offsetLimitRequest, EntityGraph entityGraph)
	{
		JPQLQuery<T> query = querydsl.applyPagination(offsetLimitRequest, createQuery(predicate).select(path));
		
		return query.select(path).fetch();
	}
	
	// Copy modified from springs QuerydslJpaPredicateExecutor
	private JPQLQuery<?> createQuery(Predicate... predicate)
	{
		AbstractJPAQuery<?, ?> query = querydsl.createQuery(path);
		
		if (predicate != null)
			query = query.where(predicate);
		
		CrudMethodMetadata metadata = getRepositoryMethodMetadata();
		if (metadata == null)
			return query;

		LockModeType type = metadata.getLockModeType();
		
		return type == null ? query : query.setLockMode(type);
	}
}
