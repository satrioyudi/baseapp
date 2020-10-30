package test.baseapp.co.id.common.stereotype.content;

public interface Content
{
	public String getPath();
	public void setPath(String path);
	
	public String getFilename();
	public void setFilename(String filename);
	
	public String getOriginalFilename();
	public void setOriginalFilename(String originalFilename);
	
	public String getMimeType();
	public void setMimeType(String mimeType);
}