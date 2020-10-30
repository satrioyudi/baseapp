package test.baseapp.co.id.common.stereotype.exception;

@SuppressWarnings("rawtypes")
public interface RootCause<L extends Log> {
	
	public void setExceptionLog(L exceptionLog);
	public L getExceptionLog();
	
	public void setExceptionClass(String exceptionClass);
	public String getExceptionClass();
	
	public void setMessage(String message);
	public String getMessage();
	
	public void setStackTrace(String stackTrace);
	public String getStackTrace();

}
