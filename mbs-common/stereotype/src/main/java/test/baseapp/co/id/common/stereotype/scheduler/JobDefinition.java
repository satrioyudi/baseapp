package test.baseapp.co.id.common.stereotype.scheduler;

import java.util.Date;

import test.baseapp.co.id.common.stereotype.HasActive;

public interface JobDefinition extends HasActive
{
	public String getCode();
	
	public String getName();
	
	public String getDescription();
	
	public String getClassName();
	
	public String getCron();
	
	public String getLastJobStatus();
	public void setLastJobStatus(String lastJobStatus);
	
	public Date getLastJobStartedAt();
	public void setLastJobStartedAt(Date lastJobStartedAt);
	
	public Date getLastJobFinishedAt();
	public void setLastJobFinishedAt(Date lastJobFinishedAtd);
}