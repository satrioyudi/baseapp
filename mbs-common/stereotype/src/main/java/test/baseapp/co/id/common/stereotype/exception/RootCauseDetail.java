package test.baseapp.co.id.common.stereotype.exception;

@SuppressWarnings("rawtypes")
public interface RootCauseDetail<R extends RootCause> {

	public void setRootCause(R rootCause);
	public R getRootCause();
	
	public void setClassName(String className);
	public String getClassName();

	public void setFileName(String fileName);
	public String getFileName();

	public void setMethodName(String methodName);
	public String getMethodName();

	public void setLineNumber(Integer lineNumber);
	public Integer getLineNumber();

}
