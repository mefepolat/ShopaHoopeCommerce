package com.shopahoop.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shopahoop.common.entity", "com.shopahoop.admin.user"})
public class ShopaHoopBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopaHoopBackEndApplication.class, args);
	}

}
