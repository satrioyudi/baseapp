package test.baseapp.co.id.common.stereotype.flowable;

import java.util.Date;

public interface IFlowHistory<U, S>
{
	public Date getDate();
	public void setDate(Date date);

	public U getUser();
	public void setUser(U user);

	public S getNewStatus();
	public void setNewStatus(S newStatus);

	public String getAction();
	public void setAction(String action);

	public String getOutput();
	public void setOutput(String output);

	public String getNote();
	public void setNote(String note);
}