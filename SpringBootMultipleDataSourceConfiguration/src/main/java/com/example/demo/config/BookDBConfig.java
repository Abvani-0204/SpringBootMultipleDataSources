package com.example.demo.config;

import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "bookEntityManagerFactory", basePackages = {
		"com.example.demo.model.book.repository" }, transactionManagerRef = "bookTransactionManager")
public class BookDBConfig {
	
	@Autowired 
	Environment env;

	@Bean(name = "bookDatasource")
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.book.datasource.driver-class-name"));
	    dataSource.setUrl(env.getProperty("spring.book.datasource.url"));
	    dataSource.setUsername(env.getProperty("spring.book.datasource.data-username"));
	    dataSource.setPassword(env.getProperty("spring.book.datasource.data-password"));

	    return dataSource;
	}

	@Bean(name = "bookEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean bookEntityManagerfactory(EntityManagerFactoryBuilder builder,
			@Qualifier("bookDatasource") DataSource dataSource) {

		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", "update");
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		return builder.dataSource(dataSource).properties(properties).packages("com.example.demo.model.book")
				.persistenceUnit("Book").build();
	}

	@Bean(name = "bookTransactionManager")
	public PlatformTransactionManager bookTransactionManager(
			@Qualifier("bookEntityManagerFactory") EntityManagerFactory bookEntityManagerFactory) {
		return new JpaTransactionManager(bookEntityManagerFactory);
	}
}
