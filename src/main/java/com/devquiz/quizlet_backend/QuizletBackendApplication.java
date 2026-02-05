package com.devquiz.quizlet_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class QuizletBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizletBackendApplication.class, args);
	}

}
