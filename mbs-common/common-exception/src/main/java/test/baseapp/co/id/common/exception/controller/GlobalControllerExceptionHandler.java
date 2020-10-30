package test.baseapp.co.id.common.exception.controller;

import java.math.BigInteger;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.Setter;
import test.baseapp.co.id.common.exception.bean.ErrorResponse;
import test.baseapp.co.id.common.exception.service.ExceptionService;
import test.baseapp.co.id.common.stereotype.exception.ExceptionType;
import test.baseapp.co.id.common.stereotype.exception.Log;

@SuppressWarnings("rawtypes")
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler{
	
	@Setter
	private String userId;
	
	private BigInteger refNumber;
	
	@Autowired
	private ExceptionService exceptionService;
	
	@ExceptionHandler
	public ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
		Log result = null;
		ex.printStackTrace();
		try {
			result = (Log) exceptionService.saveExceptionLog(ex, ExceptionType.SERVICE, userId);
			
			if(result != null)
				refNumber = (BigInteger) result.getId();
			
	        HttpHeaders headers = new HttpHeaders();
	        
	        if(refNumber != null) {
	        	headers.add("refNumber", String.valueOf(refNumber));
	        }
	        HttpStatus status = getStatus(ex);
	        
	        ErrorResponse errors = new ErrorResponse(status, ExceptionUtils.getMessage(ex), ExceptionUtils.getStackTrace(ex));
	        
	        return new ResponseEntity<>(errors, headers, errors.getStatus());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private HttpStatus getStatus(Exception ex) {
		HttpStatus status = null;
		if (ex instanceof HttpRequestMethodNotSupportedException) {
			status = HttpStatus.METHOD_NOT_ALLOWED;
		}
		else if (ex instanceof HttpMediaTypeNotSupportedException) {
			status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
		}
		else if (ex instanceof HttpMediaTypeNotAcceptableException) {
			status = HttpStatus.NOT_ACCEPTABLE;
		}
		else if (ex instanceof MissingPathVariableException) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		else if (ex instanceof MissingServletRequestParameterException) {
			status = HttpStatus.BAD_REQUEST;
		}
		else if (ex instanceof ServletRequestBindingException) {
			status = HttpStatus.BAD_REQUEST;
		}
		else if (ex instanceof ConversionNotSupportedException) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		else if (ex instanceof TypeMismatchException) {
			status = HttpStatus.BAD_REQUEST;
		}
		else if (ex instanceof HttpMessageNotReadableException) {
			status = HttpStatus.BAD_REQUEST;
		}
		else if (ex instanceof HttpMessageNotWritableException) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		else if (ex instanceof MethodArgumentNotValidException) {
			status = HttpStatus.BAD_REQUEST;
		}
		else if (ex instanceof MissingServletRequestPartException) {
			status = HttpStatus.BAD_REQUEST;
		}
		else if (ex instanceof BindException) {
			status = HttpStatus.BAD_REQUEST;
		}
		else if (ex instanceof NoHandlerFoundException) {
			status = HttpStatus.NOT_FOUND;
		}
		else if (ex instanceof AsyncRequestTimeoutException) {
			status = HttpStatus.SERVICE_UNAVAILABLE;
		}else {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return status;
	}

}
