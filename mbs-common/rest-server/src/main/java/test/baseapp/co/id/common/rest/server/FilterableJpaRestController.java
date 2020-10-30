package test.baseapp.co.id.common.rest.server;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;

public abstract class FilterableJpaRestController<T, ID extends Serializable, Q extends EntityPathBase<T>> extends JpaRestController<T, ID>
{
	@Autowired
	private QuerydslPredicateExecutor<T> repo;
	
	@Override
	public ResponseEntity<?> findAll(RestRequest restRequest) throws Exception
	{
		BooleanExpression predicate = createFilter(restRequest);
		
		Iterable<T> list = doFindAllFiltered(restRequest, predicate, repo, resourceClass);
		
		return createResponse(list, restRequest, resourceClass);
	}
	
	@Override
	@GetMapping("/count")
	public ResponseEntity<?> countAll(RestRequest restRequest) throws Exception
	{
		BooleanExpression predicate = createFilter(restRequest);
		
		return ResponseEntity.ok(createSingleValueResponse("count", repo.count(predicate)));
	}
	
	protected BooleanExpression createFilter(RestRequest restRequest) throws Exception
	{
		BooleanExpression predicate = null;
		Q pathBase = getPathBase();
		
		for(RestFilter restFilter : restEngine.processFilter(resourceClass, restRequest.getFilter()))
		{
			BooleanExpression curPredicate = createPredicate(pathBase, restFilter);
			
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
	
	protected abstract BooleanExpression createPredicate(Q pathBase, RestFilter filter);
}