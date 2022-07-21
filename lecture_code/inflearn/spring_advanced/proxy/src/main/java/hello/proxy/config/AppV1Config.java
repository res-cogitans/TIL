package hello.proxy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.proxy.app.v1.OrderControllerV1;
import hello.proxy.app.v1.OrderControllerV1Impl;
import hello.proxy.app.v1.OrderRepositoryImplV1;
import hello.proxy.app.v1.OrderRepositoryV1;
import hello.proxy.app.v1.OrderServiceV1;
import hello.proxy.app.v1.OrderServiceV1Impl;

@Configuration
public class AppV1Config {

	@Bean
	public OrderControllerV1 orderControllerV1() {
		return new OrderControllerV1Impl(orderServiceV1());
	}

	@Bean
	public OrderServiceV1 orderServiceV1() {
		return new OrderServiceV1Impl(orderRepositoryV1());
	}

	@Bean
	public OrderRepositoryV1 orderRepositoryV1() {
		return new OrderRepositoryImplV1();
	}
}
