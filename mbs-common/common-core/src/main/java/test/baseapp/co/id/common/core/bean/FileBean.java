package test.baseapp.co.id.common.core.bean;

import java.nio.file.Path;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class FileBean 
{
	private String filename;
	private String mimeType;
	private Path file;
}
