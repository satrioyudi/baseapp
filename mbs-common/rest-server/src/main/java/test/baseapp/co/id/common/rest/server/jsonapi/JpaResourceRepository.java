package test.baseapp.co.id.common.rest.server.jsonapi;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphCrudRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphPagingAndSortingRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import com.querydsl.core.types.Predicate;

import io.crnk.core.queryspec.Direction;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.queryspec.SortSpec;
import io.crnk.core.queryspec.pagingspec.NumberSizePagingSpec;
import io.crnk.core.repository.ResourceRepository;
import io.crnk.core.resource.list.DefaultResourceList;
import io.crnk.core.resource.list.ResourceList;
import io.crnk.core.resource.meta.DefaultPagedMetaInformation;

public abstract class JpaResourceRepository<T, ID extends Serializable> implements ResourceRepository<T, ID>
{
	@Autowired
	private CrudRepository<T, ID> repo;
	
	@SuppressWarnings("unchecked")
	private Class<T> resourceClass = (Class<T>) GenericTypeResolver.resolveTypeArguments(getClass(), JpaResourceRepository.class)[0];
	
	@Override
	public Class<T> getResourceClass()
	{
		return resourceClass;
	}
	
	public T findOne(ID id, QuerySpec querySpec)
	{
		if(!(repo instanceof EntityGraphCrudRepository))
			return repo.findById(id).orElse(null);
		
		return ((EntityGraphCrudRepository<T, ID>) repo).findById(id, toEntityGraph(querySpec)).orElse(null);
	}

	public ResourceList<T> findAll(QuerySpec querySpec)
	{
		Iterable<T> list = null;
		
		if(repo instanceof PagingAndSortingRepository || repo instanceof QuerydslPredicateExecutor)
		{
			Sort sort = toSort(querySpec);
			Pageable pageable = toPageable(querySpec, sort);
			if(repo instanceof PagingAndSortingRepository)
				list = findAllPagingAndSorting(querySpec, sort, pageable);
			else
				list = findAllQuerydsl(querySpec, sort, pageable);
		}
		else if(repo instanceof EntityGraphCrudRepository)
			list = ((EntityGraphCrudRepository<T, ID>) repo).findAll(toEntityGraph(querySpec));
		else
			list = repo.findAll();
		
		return toResourceList(list);
	}
	
	private Iterable<T> findAllPagingAndSorting(QuerySpec querySpec, Sort sort, Pageable pageable)
	{
		if(repo instanceof EntityGraphPagingAndSortingRepository)
		{
			EntityGraphPagingAndSortingRepository<T, ID> pagingRepo = (EntityGraphPagingAndSortingRepository<T, ID>) repo;
			if(pageable != null)
				return pagingRepo.findAll(pageable, toEntityGraph(querySpec));
			else if(sort != null)
				return pagingRepo.findAll(sort, toEntityGraph(querySpec));
			else
				return pagingRepo.findAll(toEntityGraph(querySpec));
		}
		else
		{
			PagingAndSortingRepository<T, ID> pagingRepo = (PagingAndSortingRepository<T, ID>) repo;
			if(pageable != null)
				return pagingRepo.findAll(pageable);
			else if(sort != null)
				return pagingRepo.findAll(sort);
			else
				return pagingRepo.findAll();
		}
	}
	
	private Iterable<T> findAllQuerydsl(QuerySpec querySpec, Sort sort, Pageable pageable)
	{
		if(repo instanceof EntityGraphQuerydslPredicateExecutor)
		{
			@SuppressWarnings("unchecked")
			EntityGraphQuerydslPredicateExecutor<T> dslRepo = (EntityGraphQuerydslPredicateExecutor<T>) repo;
			if(pageable != null)
				return dslRepo.findAll(null, pageable, toEntityGraph(querySpec));
			else if(sort != null)
				return dslRepo.findAll(null, sort, toEntityGraph(querySpec));
			else
				return dslRepo.findAll(null, toEntityGraph(querySpec));
		}
		else
		{
			@SuppressWarnings("unchecked")
			QuerydslPredicateExecutor<T> dslRepo = (QuerydslPredicateExecutor<T>) repo;
			if(pageable != null)
				return dslRepo.findAll(null, pageable);
			else if(sort != null)
				return dslRepo.findAll(null, sort);
			else
				return dslRepo.findAll((Predicate) null);
		}
	}

	public ResourceList<T> findAll(Collection<ID> ids, QuerySpec querySpec)
	{
		if(!(repo instanceof EntityGraphCrudRepository))
			return toResourceList(repo.findAllById(ids));
		
		return toResourceList(((EntityGraphCrudRepository<T, ID>) repo).findAllById(ids, toEntityGraph(querySpec)));
	}

	public <S extends T> S save(S resource)
	{
		return repo.save(resource);
	}

	public <S extends T> S create(S resource)
	{
		return repo.save(resource);
	}

	public void delete(ID id)
	{
		repo.deleteById(id);
	}
	
	protected Sort getDefaultSort()
	{
		return null;
	}
	
	protected ResourceList<T> toResourceList(Iterable<T> objects)
	{
		DefaultResourceList<T> resourceList = new DefaultResourceList<T>();
		
		StreamSupport.stream(objects.spliterator(), false)
				.distinct()
				.forEach(resourceList::add);
		
		if(objects instanceof Page)
		{
			Page<T> page = (Page<T>) objects;
			
			DefaultPagedMetaInformation meta = new DefaultPagedMetaInformation();
			meta.setTotalResourceCount(page.getTotalElements());
			
			resourceList.setMeta(meta);
		}
		
		return resourceList;
	}
	
	protected EntityGraph toEntityGraph(QuerySpec querySpec)
	{
		String[] attributePaths = querySpec.getIncludedRelations()
				.stream()
				.map(inc -> inc.getAttributePath().stream().collect(Collectors.joining(".")))
				.toArray(String[]::new);
		
		if(attributePaths.length > 0)
			return EntityGraphUtils.fromAttributePaths(attributePaths);
		return null;
	}
	
	protected Sort toSort(QuerySpec querySpec)
	{
		Sort sort = null;
		
		for(SortSpec sortSpec : querySpec.getSort())
		{
			String path = String.join(".", sortSpec.getAttributePath());
			Sort curSort = Sort.by(path);
			if(sortSpec.getDirection() == Direction.DESC)
				curSort = curSort.descending();
			else
				curSort = curSort.ascending();
			
			if(sort == null)
				sort = curSort;
			else
				sort = sort.and(curSort);
		};
		
		if(sort == null)
			sort = getDefaultSort();
		
		return sort;
	}
	
	protected Pageable toPageable(QuerySpec querySpec, Sort sort)
	{
		if(querySpec.getLimit() == null)
			return null;
		
		NumberSizePagingSpec pagingSpec = querySpec.getPaging(NumberSizePagingSpec.class);
		
		if(sort != null)
			return PageRequest.of(pagingSpec.getNumber() - 1, pagingSpec.getSize(), sort);
		else
			return PageRequest.of(pagingSpec.getNumber() - 1, pagingSpec.getSize());
	}
}