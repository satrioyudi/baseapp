package test.baseapp.co.id.model.util;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class Constants {
	
	public static final Map<String, Boolean> ACTIVE_MAP = ImmutableMap.<String, Boolean>builder()
			.put("active", true)
			.put("inactive", false)
			.build();
	
	public static final String DUPLICATE_DATA_MESSAGE = "Data already exist";
	public static final String EMPTY_DATA_DEFAULT_MESSAGE = "Mandatory ";
	public static final String SUCCESS_MESSAGE = "Success";
	
	public static final String FILE_NOT_FOUND = "File not found !";
}
