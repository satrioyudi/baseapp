package test.baseapp.co.id.common.stereotype.exception;

@SuppressWarnings("rawtypes")
public interface LogDetail<L extends Log>{
	
	public L getExceptionLog();
	public void setExceptionLog(L exceptionLog);
	
	public void setClassName(String className);
	public String getClassName();
	
	public void setFileName(String fileName);
	public String getFileName();
	
	public void setMethodName(String methodName);
	public String getMethodName();
	
	public void setLineNumber(Integer lineNumber);
	public Integer getLineNumber();
	
}
