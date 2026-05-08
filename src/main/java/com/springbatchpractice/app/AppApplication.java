package com.springbatchpractice.app;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//Comment the annotation below (it causes the changes not to be saved in the db) - overrides default config
//@EnableBatchProcessing
@ComponentScan({"com.springbatchpractice.config", "com.springbatchpractice.service", "com.springbatchpractice.listener",
"com.springbatchpractice.processor", "com.springbatchpractice.reader", "com.springbatchpractice.writer"})
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

}
