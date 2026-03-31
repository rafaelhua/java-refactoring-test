package com.sap.refactoring;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.SpringApplication;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class JavaRefactoringTestApplication {
	public static void main(String[] args) {
		SpringApplication.run(JavaRefactoringTestApplication.class, args);
	}
}
