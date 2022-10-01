package com.learn.moviecatalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MovieCatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieCatalogServiceApplication.class, args);
	}

	@Bean
	public RestTemplate createRestTemplateBean(){
		return new RestTemplate();
	}

}
/**
 * a bean can be created using @Bean annotation on any method inside
 * a class. The class must be added to classpath.
 * the object produced by this bean is by default singleton (i.e., one instance) and
 * can be consumed using Dependency injection (eg. using @Autowired) wherever needed.
 * @Bean is producer
 * @Autowired is consumer
 *
 * an object which is autowired looks for a bean of that type, and uses it.
 * there can't be 2 unnamed beans of same type. They have to be named.
 *
 * When is the method creating a bean executed ?
 * - https://docs.spring.io/spring-javaconfig/docs/1.0.0.M4/reference/html/ch02s02.html
 * When JavaConfig encounters such a method, it will execute that method and register the return
 * value as a bean within a BeanFactory. By default, the bean name will be the same as the method name
 *
 */