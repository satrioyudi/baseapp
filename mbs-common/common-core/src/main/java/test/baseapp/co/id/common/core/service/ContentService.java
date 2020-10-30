package test.baseapp.co.id.common.core.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ResolvableType;
import org.springframework.transaction.annotation.Transactional;

import lombok.SneakyThrows;
import test.baseapp.co.id.common.core.bean.FileBean;
import test.baseapp.co.id.common.stereotype.content.Content;
import test.baseapp.co.id.common.stereotype.content.ContentType;
import test.baseapp.co.id.common.stereotype.content.TypedContent;
import test.baseapp.co.id.common.stereotype.content.repo.ContentRepo;
import test.baseapp.co.id.common.stereotype.content.repo.ContentTypeRepo;

public class ContentService
{
	private static final Pattern TOKEN_PATTERN = Pattern.compile(".+-\\[([A-Za-z0-9-_.~%+]+)\\](?:\\[([A-Za-z0-9-_.~%+]+)\\])?");
	
	@Value("${content.tempDir:#{null}}")
	private String tempDir;
	
	@Value("${content.tempDirConfigKey:#{null}}")
	private String tempDirConfigKey;
	
	@Autowired(required = false)
	private ConfigService configService;
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private ContentRepo contentRepo;
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private ContentTypeRepo contentTypeRepo;
	
	private Class<Content> contentClass;
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init()
	{
		contentClass = (Class<Content>) ResolvableType.forInstance(contentRepo).as(ContentRepo.class).getGeneric(0).resolve();
	}
	
	public Path getTempDir()
	{
		if(tempDir != null)
			return Paths.get(tempDir);
		
		if(tempDirConfigKey != null && configService != null)
		{
			String value = configService.getConfig(tempDirConfigKey);
			if(value != null)
				return Paths.get(value);
		}
		
		throw new IllegalArgumentException("No configuration for temp dir found.");
	}
	
	public String uploadFile(InputStream input, String filename, String mimeType) throws IOException
	{
		Path dir = getTempDir();
		Files.createDirectories(dir);
		
		StringBuffer suffix = new StringBuffer("-")
				.append("[" + URLEncoder.encode(filename, StandardCharsets.UTF_8.name()) + "]");
		
		if(mimeType != null)
			suffix.append("[" + URLEncoder.encode(mimeType, StandardCharsets.UTF_8.name()) + "]");
		
		Path tmp = Files.createTempFile(dir, null, suffix.toString());
		Files.copy(input, tmp, StandardCopyOption.REPLACE_EXISTING);
		
		return tmp.getFileName().toString();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Content saveContent(String token, String destPath, Serializable contentId, Serializable contentTypeId, String... renditionTypes) throws Exception
	{
		ContentType contentType = null;
		if(contentTypeId != null)
			contentType = (ContentType) contentTypeRepo.findById(contentTypeId).orElse(null);
		
		return saveContent(token, destPath, contentId, contentType, renditionTypes);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Content saveContent(String token, String destPath, Serializable contentId, ContentType contentType, String... renditionTypes) throws Exception
	{
		Content content = null;
		if(contentId != null)
			content = (Content) contentRepo.findById(contentId).orElse(null);
		
		return saveContent(token, destPath, content, contentType, renditionTypes);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public Content saveContent(String token, String destPath, Content content, ContentType contentType, String... renditionTypes) throws Exception
	{
		// Get file from token, if not found assume no update
		FileBean uploadedFile = null;
		if(token != null)
		{
			uploadedFile = getFileFromToken(token);
			if(uploadedFile.getFile() == null || Files.notExists(uploadedFile.getFile()))
				return content;
		}
		else
			return content;
		
		// New or update
		Path oldFile = null;		
		if(content == null)
			content = contentClass.newInstance();
		else
			oldFile = Paths.get(content.getPath() + "/" + content.getFilename());	
		
		if(destPath == null)
			destPath = content.getPath();
		if(destPath == null)
			throw new IllegalArgumentException("Dest path is required.");
		
		Path dest = Paths.get(destPath);
		Files.createDirectories(dest);
		
		Path destFile = Files.createTempFile(dest, null, "-" + uploadedFile.getFilename());
		Files.move(uploadedFile.getFile(), destFile, StandardCopyOption.REPLACE_EXISTING);
		
		content.setPath(destPath);
		content.setFilename(destFile.getFileName().toString());
		content.setOriginalFilename(uploadedFile.getFilename());
		content.setMimeType(uploadedFile.getMimeType());
		
		if(content instanceof TypedContent)
			((TypedContent) content).setType(contentType);
		
		contentRepo.save(content);
		
		// Delete old file
		if(oldFile != null)
			Files.deleteIfExists(oldFile);
		
		// TODO process renditions
		
		return content;
	}
	
	@SneakyThrows
	public FileBean getFileFromToken(String token)
	{
		// Check token
		Matcher matcher = TOKEN_PATTERN.matcher(token);
		if(!matcher.matches())
			throw new IllegalArgumentException("Invalid token.");
		
		// Extract properties
		String filename = URLDecoder.decode(matcher.group(1), StandardCharsets.UTF_8.name());
		String mimeType = null;
		if(matcher.groupCount() >=3)
			mimeType = URLDecoder.decode(matcher.group(2), StandardCharsets.UTF_8.name());
		
		// Get file
		Path file = getTempDir().resolve(token);
		
		if(mimeType == null && file != null && Files.exists(file))
			mimeType = Files.probeContentType(file);
		
		return new FileBean(filename, mimeType, file);
	}
	
	@SuppressWarnings("unchecked")
	public void deleteContent(Serializable contentId) throws IOException
	{
		Content content = (Content) contentRepo.findById(contentId).orElse(null);
		if(content == null)
			throw new IllegalStateException("Content not found");
		
		deleteContent(content);
	}

	@SuppressWarnings("unchecked")
	public void deleteContent(Content content) throws IOException
	{
		Path file = Paths.get(content.getPath()).resolve(content.getFilename());
		Files.deleteIfExists(file);
		
		contentRepo.delete(content);
		
		// TODO delete renditions
	}
}