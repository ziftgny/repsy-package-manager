package com.example.repsy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.example.repsy",        // your own app code
		"com.ziftgny",
		// filesystem manager package
})
public class RepsyApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepsyApplication.class, args);
	}

}
