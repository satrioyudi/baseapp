package test.baseapp.co.id.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;

import test.baseapp.co.id.common.stereotype.repo.ExtendedEntityGraphQuerydslRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "test.baseapp.co.id.repo", repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class, repositoryBaseClass = ExtendedEntityGraphQuerydslRepository.class)
@EntityScan(basePackages = {"test.baseapp.co.id.model.entity"})
public class BusinessApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusinessApplication.class, args);
	}
}
