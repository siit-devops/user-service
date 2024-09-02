package com.devops.user_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.time.LocalDateTime;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
@EnableFeignClients
public class UserServiceApplication {

	public static void main(String[] args) {
		log.info("Welcome home Page " + LocalDateTime.now());
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
