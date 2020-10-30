package test.baseapp.co.id.common.stereotype.exception;

import java.io.Serializable;
import java.util.Date;

import test.baseapp.co.id.common.stereotype.security.User;

public interface Log <ID extends Serializable, U extends User> {
	
	public ID getId();
	
	public void setExceptionClass(String exceptionClass);
	public String getExceptionClass();
	
	public void setMessage(String message);
	public String getMessage();
	
	public void setDate(Date date);
	public Date getDate();
	
	public void setApplicationName(String applicationName);
	public String getApplicationName();
	
	public void setApplicationVersion(String applicationVersion);
	public String getApplicationVersion();
	
	public void setDeviceInfo(String deviceinfo);
	public String getDeviceInfo();
	
	public void setType(ExceptionType type);
	public ExceptionType getType();
	
	public void setUser(U user);
	public U getUser();

}
