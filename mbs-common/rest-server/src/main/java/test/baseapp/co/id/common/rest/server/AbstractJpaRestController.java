package test.baseapp.co.id.common.rest.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import com.querydsl.core.types.dsl.BooleanExpression;

import test.baseapp.co.id.common.stereotype.repo.EntityGraphOffsetLimitQuerydslPredicateExecutor;
import test.baseapp.co.id.common.stereotype.repo.OffsetLimit;
import test.baseapp.co.id.common.stereotype.repo.OffsetLimitQuerydslPredicateExecutor;

public abstract class AbstractJpaRestController
{
	@Autowired
	protected RestControllerEngine restEngine;
	
	protected <T> Iterable<T> doFindAllFiltered(RestRequest restRequest, BooleanExpression predicate, QuerydslPredicateExecutor<T> repo, Class<T> resourceClass)
	{
		Sort sort = toSort(restRequest);
		Pageable pageOrOffsetLimit = toPageableOrOffsetLimitRequest(restRequest, sort);
		
		if(repo instanceof EntityGraphQuerydslPredicateExecutor)
		{
			if(repo instanceof EntityGraphOffsetLimitQuerydslPredicateExecutor && pageOrOffsetLimit != null && pageOrOffsetLimit instanceof OffsetLimit)
			{
				EntityGraphOffsetLimitQuerydslPredicateExecutor<T> offsetLimitRepo = (EntityGraphOffsetLimitQuerydslPredicateExecutor<T>) repo;
				return offsetLimitRepo.findAllOffsetLimit(predicate, (OffsetLimit) pageOrOffsetLimit, toEntityGraph(restRequest.getExpand(), resourceClass));
			}
			else
			{
				EntityGraphQuerydslPredicateExecutor<T> graphRepo = (EntityGraphQuerydslPredicateExecutor<T>) repo;
				if(pageOrOffsetLimit != null)
					return graphRepo.findAll(predicate, pageOrOffsetLimit, toEntityGraph(restRequest.getExpand(), resourceClass));
				else if(sort != null)
					return graphRepo.findAll(predicate, sort, toEntityGraph(restRequest.getExpand(), resourceClass));
				else
					return graphRepo.findAll(predicate, toEntityGraph(restRequest.getExpand(), resourceClass));
			}
		}
		else
		{
			if(repo instanceof OffsetLimitQuerydslPredicateExecutor && pageOrOffsetLimit != null && pageOrOffsetLimit instanceof OffsetLimit)
			{
				OffsetLimitQuerydslPredicateExecutor<T> offsetLimitRepo = (OffsetLimitQuerydslPredicateExecutor<T>) repo;
				return offsetLimitRepo.findAllOffsetLimit(predicate, (OffsetLimit) pageOrOffsetLimit);
			}
			else
			{
				if(pageOrOffsetLimit != null)
					return repo.findAll(predicate, pageOrOffsetLimit);
				else if(sort != null)
					return repo.findAll(predicate, sort);
				else
					return repo.findAll(predicate);
			}
		}
	}
	
	protected EntityGraph toEntityGraph(Collection<String> paths)
	{
		return toEntityGraph(paths, null);
	}
	
	protected EntityGraph toEntityGraph(Collection<String> paths, Class<?> clazz)
	{
		if(clazz != null)
			paths = restEngine.cleanIncludes(clazz, paths);
		if(!paths.isEmpty())
			return EntityGraphUtils.fromAttributePaths(paths.toArray(new String[paths.size()]));
		return null;
	}
	
	protected Sort toSort(RestRequest restRequest)
	{
		Sort sort = null;
		
		if(restRequest.getSort() != null)
		{
			for(String sortBy : restRequest.getSort())
			{
				if(StringUtils.isBlank(sortBy))
					continue;
				
				boolean sortByDesc = sortBy.startsWith("-");
				String sortByPath = sortByDesc ? sortBy.substring(1) : sortBy;
				Sort curSort = Sort.by(sortByPath);
				if(sortByDesc)
					curSort = curSort.descending();
				else
					curSort = curSort.ascending();
				
				if(sort == null)
					sort = curSort;
				else
					sort = sort.and(curSort);
			}
		}
		
		if(sort == null)
			sort = getDefaultSort();
		
		return sort;
	}
	
	protected Sort getDefaultSort()
	{
		return null;
	}
	
	protected Pageable toPageableOrOffsetLimitRequest(RestRequest restRequest, Sort sort)
	{
		if(restRequest.isPageRequest())
			return toPageable(restRequest, sort);
		else if (restRequest.isOffsetLimitRequest())
			return toOffsetLimitRequest(restRequest, sort);
		return null;
	}
	
	protected Pageable toPageable(RestRequest restRequest, Sort sort)
	{
		if(!restRequest.isPageRequest())
			return null;
		
		if(sort != null)
			return PageRequest.of(restRequest.getPage() - 1, restRequest.getSizeOrLimit(), sort);
		else
			return PageRequest.of(restRequest.getPage() - 1, restRequest.getSizeOrLimit());
	}
	
	protected OffsetLimit toOffsetLimitRequest(RestRequest restRequest, Sort sort)
	{
		if(!restRequest.isOffsetLimitRequest())
			return null;
		
		return new OffsetLimit(restRequest.getOffset(), restRequest.getLimitOrSize(), sort);
	}
	
	protected Map<String, Object> createSingleValueResponse(String name, Object value)
	{
		Map<String, Object> response = new HashMap<String, Object>();
		response.put(name, value);
		
		return response;
	}
	
	protected <T> ResponseEntity<?> createResponse(Iterable<T> list, RestRequest restRequest, Class<T> resourceClass)
	{
		return createResponse(list, restRequest, resourceClass, null);
	}
	
	protected <T> ResponseEntity<?> createResponse(Iterable<T> list, RestRequest restRequest, Class<T> resourceClass, Function<T, ResponseWrapper<T>> wrapperFunction)
	{
		BodyBuilder response = ResponseEntity.ok();
		
		if(list instanceof Page)
		{
			Page<T> page = (Page<T>) list;
			list = page.getContent();
			
			addPaginationHeader(page, response);
		}
		
		if(wrapperFunction != null)
		{
			List<ResponseWrapper<T>> wrapped = StreamSupport.stream(list.spliterator(), false)
					.map(wrapperFunction::apply)
					.collect(Collectors.toList());
			
			return response.body(restEngine.applyJsonFilter(wrapped, resourceClass, restRequest));
		}
		else
			return response.body(restEngine.applyJsonFilter(list, resourceClass, restRequest));
	}
	
	protected <T> void addPaginationHeader(Page<T> page, BodyBuilder response)
	{
		response.header("Pagination-Page", String.valueOf(page.getNumber() + 1));
		response.header("Pagination-Size", String.valueOf(page.getSize()));
		response.header("Pagination-TotalElements", String.valueOf(page.getTotalElements()));
		response.header("Pagination-TotalPages", String.valueOf(page.getTotalPages()));
	}
}