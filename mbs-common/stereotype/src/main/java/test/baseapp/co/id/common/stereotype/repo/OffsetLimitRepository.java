package test.baseapp.co.id.common.stereotype.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OffsetLimitRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID>
{
	@Query("select e from #{#entityName} e")
	public Iterable<T> findAllOffsetLimit(OffsetLimit request);
}