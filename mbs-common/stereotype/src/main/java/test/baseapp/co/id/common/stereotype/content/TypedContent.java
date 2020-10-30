package test.baseapp.co.id.common.stereotype.content;

public interface TypedContent<CT extends ContentType> extends Content
{
	public CT getType();
	public void setType(CT type);
}