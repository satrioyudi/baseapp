package test.baseapp.co.id.common.stereotype.flowable;

import java.util.List;
import java.util.Set;

@SuppressWarnings("rawtypes")
public interface TaskFlow<T extends TaskFlow> { 
	
	public void setProcessInstanceId(String processInstanceId);
	public String getProcessInstanceId();
	
	public void setTaskId(String taskId);
	public String getTaskId();
	
	public void setTaskDefinitionKey(String taskDefintionKey);
	public String getTaskDefinitionKey();
	
	public void setCurrentTask(String currentTask);
	public String getCurrentTask();
	
	public String getInitiator();
	
	public String getOwner();

	public Set<String> getUsers();
	
	public Set<String> getGroups();
	
	public void setSubTasks(List<T> subTasks);
	public List<T> getSubTasks();
	
}
