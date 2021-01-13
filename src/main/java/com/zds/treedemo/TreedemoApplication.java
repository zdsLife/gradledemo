package com.zds.treedemo;

import com.zds.treedemo.utils.SpringFactoryUtil;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.zds.treedemo"})
@RestController
@Slf4j
@MapperScan(basePackages = {"com.zds.treedemo.mapper"})
public class TreedemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(TreedemoApplication.class, args);
		SpringFactoryUtil.setApplicationContext(applicationContext);
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
