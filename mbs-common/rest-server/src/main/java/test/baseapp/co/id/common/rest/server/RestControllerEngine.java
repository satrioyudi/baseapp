package test.baseapp.co.id.common.rest.server;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.ElementCollection;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import test.baseapp.co.id.common.rest.server.RestFilter.Operator;
import test.baseapp.co.id.common.util.TypeConverter;

@Component
public class RestControllerEngine
{
	private Map<String, TypeRegister> registry = new HashMap<String, TypeRegister>();
	
	public void registerClass(Class<?> clazz)
	{
		doRegisterClass(clazz);
	}
	
	private TypeRegister doRegisterClass(Class<?> clazz)
	{
		if(registry.containsKey(clazz.getName()))
			return registry.get(clazz.getName());
		
		JsonFilter jsonFilter = clazz.getAnnotation(JsonFilter.class);
		if(jsonFilter == null)
			return null;
		
		TypeRegister register = new TypeRegister();
		registry.put(clazz.getName(), register);
		
		register.setClazz(clazz);
		register.setClassName(clazz.getName());
		register.setFilterName(jsonFilter.value());
		
		findGetters(clazz).forEach(m -> addField(register, m));
		
		JsonSubTypes jsonSubTypes = clazz.getAnnotation(JsonSubTypes.class);
		if(jsonSubTypes != null)
		{
			for(Type subType : jsonSubTypes.value())
			{
				Class<?> subClass = subType.value();
				TypeRegister subRegister = doRegisterClass(subClass);
				if(subRegister != null)
					register.getSubTypes().add(subRegister);
			}
		}
		
		return register;
	}
	
	private Set<Method> findGetters(Class<?> clazz)
	{
		Set<Method> allGetters = new HashSet<Method>();
		doFindGetters(clazz, allGetters);
		
		return allGetters;
	}
	
	private void doFindGetters(Class<?> clazz, Set<Method> allGetters)
	{
		Set<Method> methods = new HashSet<Method>();
		ReflectionUtils.doWithLocalMethods(clazz, methods::add);
		
		methods.stream()
				.filter(this::isGetter)
				.filter(m -> !m.isBridge())
				.filter(m -> !allGetters.stream().anyMatch(g -> g.getName().equals(m.getName())))
				.forEach(allGetters::add);
		
		Class<?> superClass = clazz.getSuperclass();
		if(superClass != null)
			doFindGetters(superClass, allGetters);
	}
	
	private boolean isGetter(Method method)
	{
		if(Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0 && !method.getName().equals("getClass"))
		{
			if (method.getName().matches("^get[A-Z].*") && !method.getReturnType().equals(void.class))
				return true;
			if (method.getName().matches("^is[A-Z].*") && method.getReturnType().equals(boolean.class))
				return true;
		}
		
		return false;
	}
	
	private void addField(TypeRegister register, Method method)
	{
		String fieldName = toFieldName(method.getName());
		
		Class<?> fieldClass = null;
		
		if(Iterable.class.isAssignableFrom(method.getReturnType()))
			fieldClass = (Class<?>) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
		else
			fieldClass = method.getReturnType();
		
		TypeRegisterField registerField = new TypeRegisterField();
		registerField.setName(fieldName);
		registerField.setClazz(fieldClass);
		registerField.setClassName(fieldClass.getName());
		
		if(isRelation(method))
			registerField.setIsRelationship(true);
		
		registerField.setRegister(doRegisterClass(fieldClass));
		
		register.getFields().add(registerField);
	}
	
	private String toFieldName(String methodName)
	{
		String name = null;
		if (methodName.startsWith("get"))
			name = StringUtils.removeStart(methodName, "get");
		if (methodName.startsWith("is"))
			name = StringUtils.removeStart(methodName, "is");
		
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}
	
	private boolean isRelation(Method method)
	{
		boolean isRelation = objectIsRelation(method);
		
		if(!isRelation)
		{
			Field field = ReflectionUtils.findField(method.getDeclaringClass(), toFieldName(method.getName()));
			if(field != null)
				isRelation = objectIsRelation(field);
		}
		
		return isRelation;
	}
	
	private boolean objectIsRelation(AccessibleObject object)
	{
		return object.isAnnotationPresent(ManyToOne.class)
				|| object.isAnnotationPresent(OneToMany.class)
				|| object.isAnnotationPresent(OneToOne.class)
				|| object.isAnnotationPresent(ManyToMany.class)
				|| object.isAnnotationPresent(ElementCollection.class);
	}
	
	public MappingJacksonValue applyJsonFilter(Object object, RestRequest restRequest)
	{
		return applyJsonFilter(object, object.getClass(), restRequest);
	}
	
	public MappingJacksonValue applyJsonFilter(Object object, Class<?> objectClass, RestRequest restRequest)
	{
		return applyJsonFilter(object, objectClass, restRequest.getExpand(), restRequest.getInclude(), restRequest.getExclude());
	}
	
	public MappingJacksonValue applyJsonFilter(Object object, Class<?> objectClass, Set<String> includedRelations, Set<String> includedFields, Set<String> excludedFields)
	{
		MappingJacksonValue mapping = new MappingJacksonValue(object);
		
		String className = objectClass.getName();
		TypeRegister register = registry.get(className);
		
		if(register != null)
		{
			Set<FilterHolder> filters = new HashSet<FilterHolder>();
			applyJsonFilter(filters, register, includedRelations, includedFields, excludedFields);
			
			Map<String, List<FilterHolder>> groupedFilters = filters.stream().collect(Collectors.groupingBy(FilterHolder::getFilterName));
			filters.clear();
			for(List<FilterHolder> curFilters : groupedFilters.values())
			{
				FilterHolder filter = curFilters.get(0);
				for(int i = 1; i < curFilters.size(); i++)
				{
					FilterHolder curFilter = curFilters.get(i);
					filter.getIncluded().addAll(curFilter.included);
					filter.getExcluded().addAll(curFilter.getExcluded());
					filter.getNotExcluded().addAll(curFilter.getNotExcluded());
				}
				filters.add(filter);
			}
			
			filters.forEach(f -> System.out.println(f.getFilterName()+":"+f.getFinalIncludes()));
			
			SimpleFilterProvider filterProvider = new SimpleFilterProvider();
			
			for(FilterHolder filter : filters)
				filterProvider.addFilter(filter.getFilterName(), SimpleBeanPropertyFilter.filterOutAllExcept(filter.getFinalIncludes()));
			
			filterProvider.setFailOnUnknownId(false);
			
			mapping.setFilters(filterProvider);
		}
				
		return mapping;
	}
	
	private void applyJsonFilter(Set<FilterHolder> filters, TypeRegister register, Set<String> includedRelations, Set<String> includedFields, Set<String> excludedFields)
	{
		FilterHolder filter = createFilterHolder(register);
		
		filters.add(filter);
		
		filter.getNotExcluded().addAll(getRootNames(includedRelations));
		filter.getExcluded().addAll(getRootNames(excludedFields));
		filter.getIncluded().addAll(getRootNames(includedFields));
		
		for(String include : filter.getFinalIncludes())
		{
			TypeRegister includeRegister = register.getFields().stream()
					.filter(r -> r.getName().equals(include))
					.findFirst()
					.map(TypeRegisterField::getRegister)
					.orElse(null);
			
			if(includeRegister != null)
				applyJsonFilter(filters, includeRegister, 
						getSubNames(includedRelations, include), getSubNames(includedFields, include), getSubNames(excludedFields, include));
		}
		
		for(TypeRegister subRegister : register.getSubTypes())
		{
			applyJsonFilter(filters, subRegister, includedRelations, includedFields, excludedFields);
		}
	}
	
	private FilterHolder createFilterHolder(TypeRegister register)
	{
		FilterHolder filter = new FilterHolder();
		
		filter.setFilterName(register.getFilterName());
		
		register.getFields().forEach(f -> filter.getFields().add(f.getName()));
		
		filter.getExcluded().add("hibernateLazyInitializer");
		
		register.getFields().stream()
				.filter(f -> f.getIsRelationship())
				.forEach(f -> filter.getExcluded().add(f.getName()));
		
		return filter;
	}
	
	private Set<String> getRootNames(Collection<String> names)
	{
		if(names == null || names.isEmpty())
			return Collections.emptySet();
		
		return names.stream()
				.map(i -> StringUtils.substringBefore(i, "."))
				.distinct()
				.collect(Collectors.toSet());
	}
	
	private Set<String> getSubNames(Collection<String> names, String rootName)
	{
		if(names == null || names.isEmpty())
			return Collections.emptySet();
		
		return names.stream()
				.filter(n -> n.startsWith(rootName + "."))
				.map(n -> StringUtils.substringAfter(n, rootName + "."))
				.collect(Collectors.toSet());
	}
	
	public Set<String> cleanIncludes(Class<?> clazz, Collection<String> includes)
	{
		return cleanIncludes(clazz, includes, null);
	}
	
	public Set<String> cleanIncludes(Class<?> clazz, Collection<String> includes, String prefix)
	{
		if(StringUtils.isNotBlank(prefix) && includes != null)
		{
			String fPrefix = prefix.endsWith(".") ? prefix : prefix + ".";
			includes = includes.stream()
					.filter(i -> i.startsWith(fPrefix))
					.map(i -> StringUtils.removeStart(i, fPrefix))
					.collect(Collectors.toSet());
		}
		
		TypeRegister register = registry.get(clazz.getName());
		
		Set<String> cleanIncludes = cleanIncludes(register, includes);
		
		return cleanIncludes;
	}
	
	public Set<String> cleanIncludes(TypeRegister register, Collection<String> includes)
	{
		Set<String> cleanIncludes = new HashSet<String>();
		
		if(register != null)
		{
			for(String rootName : getRootNames(includes))
			{
				TypeRegisterField registerField = register.getFields().stream()
						.filter(f -> f.getName().equals(rootName) && f.getIsRelationship())
						.findFirst().orElse(null);
				
				if(registerField != null)
				{
					Set<String> subs = cleanIncludes(registerField.getRegister(), getSubNames(includes, rootName));
					if(subs.isEmpty())
						cleanIncludes.add(registerField.getName());
					else
						subs.stream()
								.map(s -> registerField.getName() + "." + s)
								.forEach(cleanIncludes::add);
				}
			}
		}
		
		return cleanIncludes;
	}
	
	public Set<RestFilter> processFilter(Class<?> clazz, Map<String, String[]> rawFilter) throws ParseException, ReflectiveOperationException
	{
		if(rawFilter == null)
			return Collections.emptySet();
		
		Set<RestFilter> filters = new HashSet<RestFilter>();
		
		Pattern operatorRegex = Pattern.compile("\\[((?i)eq|ne|lt|le|gt|ge)\\](.*)");
		for(Entry<String, String[]> entry : rawFilter.entrySet())
		{
			for(String rawValue : entry.getValue())
			{
				RestFilter filter = new RestFilter();
				
				String path = entry.getKey();	
				
				filter.setPath(path);
				
				Matcher operatorMatcher = operatorRegex.matcher(rawValue);
				if(operatorMatcher.matches())
				{
					filter.setOperator(Operator.valueOf(operatorMatcher.group(1)));
					rawValue = operatorMatcher.group(2);
				}
				else
					filter.setOperator(Operator.EQ);
				
				if(StringUtils.isNotEmpty(rawValue))
				{
					TypeRegisterField field = findField(clazz, path);
					Object value = rawValue;
					if(field != null)
						value = TypeConverter.convertTo(rawValue, field.getClazz());
					filter.setValue(value);
				}
				
				// Group equals filter together
				RestFilter eqFilter = filters.stream()
						.filter(f -> f.getPath().equals(filter.getPath()) && f.getOperator() == filter.getOperator())
						.findFirst().orElse(null);
				if(eqFilter != null)
				{
					if(eqFilter.getValue() instanceof Collection)
						((Collection<?>) eqFilter.getValue()).add(filter.getValue());
					else
					{
						Set<Object> values = new HashSet<Object>();
						values.add(eqFilter.getValue());
						values.add(filter.getValue());
						eqFilter.setValue(values);
					}
				}
				else
					filters.add(filter);
			}
		}
		
		return filters;
	}
	
	private TypeRegisterField findField(Class<?> clazz, String path)
	{
		String root = StringUtils.substringBefore(path, ".");
		String leaf = StringUtils.trimToNull(StringUtils.substringAfter(path, root + "."));
		
		TypeRegister register = registry.get(clazz.getName());
		TypeRegisterField registerField = null;
		if(register != null)
		{
			registerField = register.getFields().stream()
					.filter(f -> f.getName().equals(root))
					.findFirst().orElse(null);
			
			if(registerField != null && leaf != null)
				registerField = findField(registerField.getClazz(), leaf);
		}
		
		return registerField;
	}
	
	@Getter @Setter
	private class TypeRegister
	{
		private String filterName;
		private Class<?> clazz;
		private String className;
		private Set<TypeRegister> subTypes = new HashSet<TypeRegister>();
		private Set<TypeRegisterField> fields = new HashSet<TypeRegisterField>();
	}
	
	@Getter @Setter @ToString
	private class TypeRegisterField
	{
		private String name;
		private Class<?> clazz;
		private String className;
		private Boolean isRelationship = false;
		private Boolean isToMany = false;
		private TypeRegister register;
	}
	
	@Getter @Setter
	private class FilterHolder
	{
		private String filterName;
		private Set<String> fields = new HashSet<String>();
		private Set<String> included = new HashSet<String>();
		private Set<String> excluded = new HashSet<String>();
		private Set<String> notExcluded = new HashSet<String>();
		
		public Set<String> getFinalIncludes()
		{
			Set<String> result = new HashSet<String>();
			if(!included.isEmpty())
			{
				result.addAll(included);
				result.addAll(notExcluded);
			}
			else
			{
				result.addAll(fields);
				result.removeAll(excluded);
				result.addAll(notExcluded);
			}
			
			return result;
		}
	}
}
