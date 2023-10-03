package com.cumpleanos.consumowsdl;



import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class ConsumowsdlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumowsdlApplication.class, args);
	}

}

