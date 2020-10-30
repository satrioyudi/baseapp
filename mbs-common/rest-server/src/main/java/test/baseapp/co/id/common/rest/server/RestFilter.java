package test.baseapp.co.id.common.rest.server;

import lombok.Data;

@Data
public class RestFilter
{
	public enum Operator
	{
		EQ,
		NE,
		LE,
		LT,
		GE,
		GT
	}
	
	private String path;
	private Operator operator;
	private Object value;
	
	@SuppressWarnings("unchecked")
	public <T> T getValue() {
		return (T) this.value;
	}
}