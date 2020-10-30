package test.baseapp.co.id.common.stereotype.eav;

import java.util.List;
import java.util.Map;

public interface IAttribute
{
	public String getName();
	public void setName(String name);

	public String getType();
	public void setType(String type);
	
	public String getInputType();
	public void setInputType(String inputType);
	
	public abstract Map<String, String> getProperties();
	public abstract void setProperties(Map<String, String> values);
	
	public abstract List<String> getValues();
	public abstract void setValues(List<String> values);
}