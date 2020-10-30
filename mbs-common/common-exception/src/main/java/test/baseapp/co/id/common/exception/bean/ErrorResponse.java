package test.baseapp.co.id.common.exception.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ErrorResponse {

	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	private HttpStatus status;
    private String message;
    private List<String> errors;
    private String timestamp;
    
    public ErrorResponse(HttpStatus status, String message, List<String> errors) {
    	this.timestamp = dateFormat.format(new Date());
    	this.status = status;
    	this.message = message;
    	this.errors = errors;
    }
    
    public ErrorResponse(HttpStatus status, String message, String error) {
    	this.timestamp = dateFormat.format(new Date());
    	this.status = status;
    	this.message = message;
    	this.errors = Arrays.asList(error);
    }
	
	

}
