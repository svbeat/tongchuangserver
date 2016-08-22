package com.tongchuang.visiondemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}
	
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2) 
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("com.tongchuang.visiondemo"))              
          .paths(PathSelectors.regex("/patients.*|/doctors.*|/devices.*|/relationships.*|/userroles.*|/login.*"))  
          //.paths(PathSelectors.any())
          .build();                                           
    }
	
/*
	@Bean
	public CommandLineRunner demo(final PerimetryExamRepository repository) {
		return new CommandLineRunner() {
			@Override
		    public void run(String... args) throws Exception {
				// save a couple of customers
				repository.save(new PerimetryExam("Jack", "Bauer"));
				repository.save(new PerimetryExam("Chloe", "O'Brian"));
				repository.save(new PerimetryExam("Kim", "Bauer"));
				repository.save(new PerimetryExam("David", "Palmer"));
				repository.save(new PerimetryExam("Michelle", "Dessler"));

				// fetch all customers
				log.info("Customers found with findAll():");
				log.info("-------------------------------");
				for (PerimetryExam exam : repository.findAll()) {
					log.info(exam.toString());
				}
	            log.info("");

				// fetch an individual customer by ID
	            PerimetryExam exam = repository.findOne(1L);
				log.info("Customer found with findOne(1L):");
				log.info("--------------------------------");
				log.info(exam.toString());
	            log.info("");

				// fetch customers by last name
				log.info("Customer found with findByLastName('Bauer'):");
				log.info("--------------------------------------------");
				for (PerimetryExam bauer : repository.findByPatientId("Bauer")) {
					log.info(bauer.toString());
				}
	            log.info("");
			};

		};
	}
*/
	
}