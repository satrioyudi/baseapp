package test.baseapp.co.id.common.rest.server.jsonapi;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;

import io.crnk.core.queryspec.FilterSpec;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.resource.list.ResourceList;

public abstract class FilterableJpaResourceRepository<T, ID extends Serializable, Q extends EntityPathBase<T>> extends JpaResourceRepository<T, ID>
{
	@Autowired
	private QuerydslPredicateExecutor<T> repo;
	
	@Override
	public ResourceList<T> findAll(QuerySpec querySpec)
	{
		Iterable<T> list = null;
		
		Sort sort = toSort(querySpec);
		Pageable pageable = toPageable(querySpec, sort);
		BooleanExpression predicate = createFilter(querySpec);
		
		if(repo instanceof EntityGraphQuerydslPredicateExecutor)
		{
			EntityGraphQuerydslPredicateExecutor<T> graphRepo = (EntityGraphQuerydslPredicateExecutor<T>) repo;
			if(pageable != null)
				list = graphRepo.findAll(predicate, pageable, toEntityGraph(querySpec));
			else if(sort != null)
				list = graphRepo.findAll(predicate, sort, toEntityGraph(querySpec));
			else
				list = graphRepo.findAll(predicate, toEntityGraph(querySpec));
		}
		else
		{
			if(pageable != null)
				list = repo.findAll(predicate, pageable);
			else if(sort != null)
				list = repo.findAll(predicate, sort);
			else
				list = repo.findAll(predicate);
		}
		
		return toResourceList(list);
	}
	
	protected BooleanExpression createFilter(QuerySpec querySpec)
	{
		BooleanExpression predicate = null;
		Q pathBase = getPathBase();
		
		for(FilterSpec filter : querySpec.getFilters())
		{
			String path = String.join(".", filter.getAttributePath());
			BooleanExpression curPredicate = createPredicate(pathBase, path, filter);
			
			if(curPredicate != null)
			{
				if(predicate == null)
					predicate = curPredicate;
				else
					predicate = predicate.and(curPredicate);
			}
		}
		
		return predicate;
	}
	
	protected abstract Q getPathBase();
	
	protected abstract BooleanExpression createPredicate(Q pathBase, String path, FilterSpec filterSpec);
}