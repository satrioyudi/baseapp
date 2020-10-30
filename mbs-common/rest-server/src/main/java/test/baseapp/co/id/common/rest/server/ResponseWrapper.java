package test.baseapp.co.id.common.rest.server;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonFilter("responseWrapper.filter")
public class ResponseWrapper<T>
{
	@JsonUnwrapped
	private T object;
}