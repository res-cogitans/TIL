package org.programmers.kdtspringorder;

import org.programmers.kdtspringorder.order.OrderProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.text.MessageFormat;

@SpringBootApplication
public class KdtSpringOrderApplication {

	public static void main(String[] args) {
		var springApplication = new SpringApplication(KdtSpringOrderApplication.class);
		springApplication.setAdditionalProfiles();
		var applicationContext = springApplication.run(args);
		var orderProperties = applicationContext.getBean(OrderProperties.class);

		System.out.println(MessageFormat.format("orderProperties.getVersion() = {0}",
				orderProperties.getVersion()));
		System.out.println(MessageFormat.format("orderProperties.getDescription() = {0}",
				orderProperties.getDescription()));
		System.out.println(MessageFormat.format("orderProperties.getVersion() = {0}",
				orderProperties.getVersion()));
		System.out.println(MessageFormat.format("orderProperties.getMinimumOrderAmount() = {0}",
				orderProperties.getMinimumOrderAmount()));
		System.out.println(MessageFormat.format("orderProperties.getJavaHome() = {0}",
				orderProperties.getJavaHome()));
		System.out.println(MessageFormat.format("orderProperties.getSupportVendors() = {0}",
				orderProperties.getSupportVendors()));

	}

}
