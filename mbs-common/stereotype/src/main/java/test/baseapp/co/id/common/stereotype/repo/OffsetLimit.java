package test.baseapp.co.id.common.stereotype.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetLimit implements Pageable
{
	private long offset;
	private int limit;	
	
	private Sort sort;
	
	public OffsetLimit(long offset, int limit)
	{
		super();
		this.offset = offset;
		this.limit = limit;
	}

	public OffsetLimit(long offset, int limit, Sort sort)
	{
		super();
		this.offset = offset;
		this.limit = limit;
		this.sort = sort;
	}

	@Override
	public int getPageNumber()
	{
		return (int) offset / limit;
	}

	@Override
	public int getPageSize()
	{
		return limit;
	}

	@Override
	public long getOffset()
	{
		return offset;
	}

	@Override
	public Sort getSort()
	{
		if(sort != null)
			return sort;
		else 
			return Sort.unsorted();
	}

	@Override
	public Pageable next()
	{
		return new OffsetLimit(offset + getPageSize(), getPageSize(), sort);
	}

	@Override
	public Pageable previousOrFirst()
	{
		return hasPrevious() ? new OffsetLimit(offset - getPageSize(), getPageSize(), sort) : first();
	}

	@Override
	public Pageable first()
	{
		return new OffsetLimit(0, getPageSize());
	}

	@Override
	public boolean hasPrevious()
	{
		return offset > limit;
	}

}
