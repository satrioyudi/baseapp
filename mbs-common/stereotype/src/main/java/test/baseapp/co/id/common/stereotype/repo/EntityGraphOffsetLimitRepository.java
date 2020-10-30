package test.baseapp.co.id.common.stereotype.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.Query;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphPagingAndSortingRepository;

public interface EntityGraphOffsetLimitRepository<T, ID extends Serializable> extends OffsetLimitRepository<T, ID>, EntityGraphPagingAndSortingRepository<T, ID>
{
	@Query("select e from #{#entityName} e")
	public Iterable<T> findAllOffsetLimit(OffsetLimit request, EntityGraph entityGraph);
}
