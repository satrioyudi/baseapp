package test.baseapp.co.id.common.stereotype.utils;

import java.io.Serializable;
import java.util.stream.Stream;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.data.repository.CrudRepository;

public class StereotypeUtils
{
	@SuppressWarnings("unchecked")
	public static <T> CrudRepository<T, Serializable> findRepoForEntity(ApplicationContext context, Class<T> entityClass)
	{
		ResolvableType repoType = ResolvableType.forClassWithGenerics(CrudRepository.class, 
				ResolvableType.forType(TypeUtils.wildcardType().withUpperBounds(entityClass).build()),
				ResolvableType.forType(TypeUtils.wildcardType().withUpperBounds(Serializable.class).build()));
		
		return Stream.of(context.getBeanNamesForType(repoType))
				.findFirst()
				.map(context::getBean)
				.map(CrudRepository.class::cast)
				.orElseThrow(() -> new NoSuchBeanDefinitionException(repoType));
	}
}