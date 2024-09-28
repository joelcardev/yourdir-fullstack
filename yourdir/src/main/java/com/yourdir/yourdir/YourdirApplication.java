package com.yourdir.yourdir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class YourdirApplication {

	public static void main(String[] args) {
		SpringApplication.run(YourdirApplication.class, args);
	}

}
