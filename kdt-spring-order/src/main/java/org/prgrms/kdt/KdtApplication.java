package org.prgrms.kdt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.prgrms.kdt.customer", "org.prgrms.kdt.config"})
public class KdtApplication {
	public static void main(String[] args) {
		SpringApplication.run(KdtApplication.class, args);
	}
}
