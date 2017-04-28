package com.npwit.poc.cielo.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

@SpringBootApplication
@EnableDiscoveryClient
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}

	@Bean
	public ServletContextInitializer servletInitializer() {
		return new ServletContextInitializer() {

			@Override
			public void onStartup(ServletContext servletContext) throws ServletException {
				final ServletRegistration.Dynamic appServlet = servletContext.addServlet("jersey-servlet", new SpringServlet());
				Map<String, String> filterParameters = new HashMap<>();
				// Set filter parameters
				filterParameters.put("javax.ws.rs.Application", "com.npwit.poc.cielo.config.PocResourcesConfig");
				appServlet.setInitParameters(filterParameters);
				appServlet.setLoadOnStartup(2);
				appServlet.addMapping("/*");
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}