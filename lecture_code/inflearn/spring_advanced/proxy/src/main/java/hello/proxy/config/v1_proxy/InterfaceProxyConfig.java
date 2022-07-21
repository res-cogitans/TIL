package hello.proxy.config.v1_proxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.proxy.app.v1.OrderControllerV1;
import hello.proxy.app.v1.OrderControllerV1Impl;
import hello.proxy.app.v1.OrderRepositoryImplV1;
import hello.proxy.app.v1.OrderRepositoryV1;
import hello.proxy.app.v1.OrderServiceV1;
import hello.proxy.app.v1.OrderServiceV1Impl;
import hello.proxy.config.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import hello.proxy.trace.logtrace.LogTrace;

@Configuration
public class InterfaceProxyConfig {

	@Bean
	public OrderControllerV1 orderController(LogTrace logTrace) {
		return new OrderControllerInterfaceProxy(
			new OrderControllerV1Impl(orderService(logTrace)), logTrace);
	}

	@Bean
	public OrderServiceV1 orderService(LogTrace logTrace) {
		return new OrderServiceInterfaceProxy(
			new OrderServiceV1Impl(orderRepository(logTrace)), logTrace);
	}

	@Bean
	public OrderRepositoryV1 orderRepository(LogTrace logTrace) {
		return new OrderRepositoryInterfaceProxy(
			new OrderRepositoryImplV1(), logTrace);
	}
}
