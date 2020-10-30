package test.baseapp.co.id.common.stereotype.content;

public interface ContentRendition<C extends Content>
{
	public C getOwningContent();
	public void setOwningContent(C owningContent);
	
	public C getContent();
	public void setContent(C content);
	
	public String getType();
	public void setType(String type);
}