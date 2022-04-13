package com.example.securedapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(Saml2LoginBootConfiguration.class)
public class SecuredAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecuredAppApplication.class, args);
	}

}
