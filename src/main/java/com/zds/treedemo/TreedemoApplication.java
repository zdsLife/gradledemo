package com.zds.treedemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.zds.treedemo"})
@RestController
@Slf4j
public class TreedemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TreedemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("服务启动完成");
	}

	@RequestMapping("/")
	String home() {
		return "index";
	}
}
