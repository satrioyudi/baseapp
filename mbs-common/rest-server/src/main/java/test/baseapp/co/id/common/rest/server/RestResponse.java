package test.baseapp.co.id.common.rest.server;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Getter;
import lombok.Setter;

@JsonFilter("restResponse.filter")
@Getter @Setter
public class RestResponse<T> {

	private HttpStatus status;
	
	private String message;
	
	private T data;
	
	private Boolean isValid;
	
}
