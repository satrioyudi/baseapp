package test.baseapp.co.id.common.stereotype.flowable;

public interface IFlow<S>
{
	public S getStatus();	
	public void setStatus(S status);
	
	public String getProcessInstanceId();
	public void setProcessInstanceId(String processInstanceId);
}