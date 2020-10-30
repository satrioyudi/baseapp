package test.baseapp.co.id.common.util;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.ReflectionUtils;

public class TypeConverter
{
	private static final String[] DATE_FORMATS = new String[] {
			"yyyy-MM-dd",
			"yyyy-MM-dd'T'HH:mm:ss",
			"yyyy-MM-dd'T'HH:mm:ss.SSS",
			"yyyy-MM-dd'T'HH:mm:ss'Z'",
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
			"yyyy-MM-dd'T'HH:mm:ssZZZ",
			"yyyy-MM-dd'T'HH:mm:ss.SSSZZZ"
	};
	
	private static Map<String, Constructor<?>> constructorRegistry = new HashMap<String, Constructor<?>>();
	
	@SuppressWarnings("unchecked")
	public static <T> T convertTo(Object value, Class<T> clazz) throws ParseException, ReflectiveOperationException
	{
		if(value == null)
			return null;
		
		if(clazz.isAssignableFrom(value.getClass()))
			return (T) value;
		
		Object finalValue = null;
		String stringValue = value.toString();
		
		if(clazz.equals(String.class))
			finalValue = stringValue;
		else if(clazz.equals(Boolean.class))
		{
			if(StringUtils.isNumeric(stringValue))
				finalValue = BooleanUtils.toBoolean(Integer.valueOf(stringValue));
			else
				finalValue = BooleanUtils.toBoolean(stringValue);
		}
		else if(clazz.equals(Integer.class) || clazz.equals(int.class))
			finalValue = Integer.valueOf(stringValue);
		else if(clazz.equals(Long.class) || clazz.equals(long.class))
			finalValue = Long.valueOf(stringValue);
		else if(clazz.equals(Double.class) || clazz.equals(double.class))
			finalValue = Double.valueOf(stringValue);
		else if(clazz.equals(BigInteger.class))
			finalValue = new BigInteger(stringValue);
		else if(clazz.equals(BigDecimal.class))
			finalValue = new BigDecimal(stringValue);
		else if(clazz.equals(Date.class) || Date.class.isAssignableFrom(clazz))
		{
			Date date = null;
			if(StringUtils.isNumeric(stringValue))
				date = new Date(Long.valueOf(stringValue));
			else
				date = DateUtils.parseDateStrictly(stringValue, DATE_FORMATS);
			
			if(clazz.equals(Date.class))
				finalValue = date;
			else
			{
				Constructor<?> constructor = null;
				if(!constructorRegistry.containsKey(clazz.getName()))
				{
					// No param constructor
					constructor = findConstructor(clazz, null);
					// Long param constructor
					if(constructor == null)
						constructor = findConstructor(clazz, long.class);
					if(constructor == null)
						constructor = findConstructor(clazz, Long.class);
					
					constructorRegistry.put(clazz.getName(), constructor);
				}
				else
					constructor = constructorRegistry.get(clazz.getName());
				
				if(constructor == null)
					finalValue = stringValue;
				else if(constructor.getParameterCount() == 0)
				{
					Date sub = (Date) constructor.newInstance();
					sub.setTime(date.getTime());
					
					finalValue = sub;
				}
				else
					finalValue = constructor.newInstance(date.getTime());
			}
		}
		else
		{
			// Find String constructor
			Constructor<?> constructor = null;
			if(!constructorRegistry.containsKey(clazz.getName()))
			{
				constructor = findConstructor(clazz, String.class);
				
				constructorRegistry.put(clazz.getName(), constructor);
			}
			else
				constructor = constructorRegistry.get(clazz.getName());
			
			if(constructor != null)
				finalValue = constructor.newInstance(stringValue);
			else
				finalValue = stringValue;
			
		}
		
		return (T) finalValue;
	}
	
	private static Constructor<?> findConstructor(Class<?> clazz, Class<?> paramType)
	{
		try
		{
			if(paramType == null)
				return ReflectionUtils.accessibleConstructor(clazz);
			else	
				return ReflectionUtils.accessibleConstructor(clazz, paramType);
		}
		catch (NoSuchMethodException e)
		{
			return null;
		}
	}
}