package test.baseapp.co.id.common.rest.server;

import java.util.Map;
import java.util.Set;

import lombok.Data;

@Data
public class RestRequest
{
	private Set<String> expand;
	private Set<String> include;
	private Set<String> exclude;
	
	private Map<String, String[]> filter;
	
	private Set<String> sort;
	
	private Integer page;
	private Integer size = 25;
	
	private Integer offset;
	private Integer limit = 25;
	
	public Integer getSizeOrLimit()
	{
		return size != null ? size : limit;
	}
	
	public Integer getLimitOrSize()
	{
		return limit != null ? limit : size;
	}
	
	public boolean isPageRequest()
	{
		return page != null && getSizeOrLimit() != null;
	}
	
	public boolean isOffsetLimitRequest()
	{
		return offset != null && getLimitOrSize() != null;
	}
}