package test.baseapp.co.id.common.stereotype;

public interface ValueProvider<T>
{
	public T findById(String objectId);
	public Object getIdentifier(T value);
}