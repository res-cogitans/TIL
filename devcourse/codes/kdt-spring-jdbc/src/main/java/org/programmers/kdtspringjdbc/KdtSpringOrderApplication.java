package org.programmers.kdtspringjdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KdtSpringOrderApplication {

	public static void main(String[] args) {
		var springApplication = new SpringApplication(KdtSpringOrderApplication.class);
		springApplication.setAdditionalProfiles("dev");
		var applicationContext = springApplication.run(args);
	}
}
