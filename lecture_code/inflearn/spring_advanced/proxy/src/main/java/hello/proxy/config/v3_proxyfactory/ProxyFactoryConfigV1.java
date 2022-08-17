package hello.proxy.config.v3_proxyfactory;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.proxy.app.v1.OrderControllerV1;
import hello.proxy.app.v1.OrderControllerV1Impl;
import hello.proxy.app.v1.OrderRepositoryImplV1;
import hello.proxy.app.v1.OrderRepositoryV1;
import hello.proxy.app.v1.OrderServiceV1;
import hello.proxy.app.v1.OrderServiceV1Impl;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ProxyFactoryConfigV1 {

	@Bean
	public OrderControllerV1 orderControllerV1(LogTrace logTrace) {
		OrderControllerV1 orderController = new OrderControllerV1Impl(orderServiceV1(logTrace));

		ProxyFactory proxyFactory = new ProxyFactory(orderController);
		proxyFactory.addAdvisor(getAdvisor(logTrace));

		return (OrderControllerV1)proxyFactory.getProxy();
	}

	@Bean
	public OrderServiceV1 orderServiceV1(LogTrace logTrace) {
		OrderServiceV1 orderService = new OrderServiceV1Impl(orderRepositoryV1(logTrace));

		ProxyFactory proxyFactory = new ProxyFactory(orderService);
		proxyFactory.addAdvisor(getAdvisor(logTrace));

		return (OrderServiceV1)proxyFactory.getProxy();
	}

	@Bean
	public OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace) {
		OrderRepositoryV1 orderRepository = new OrderRepositoryImplV1();

		ProxyFactory proxyFactory = new ProxyFactory(orderRepository);
		proxyFactory.addAdvisor(getAdvisor(logTrace));

		return (OrderRepositoryV1)proxyFactory.getProxy();
	}

	private Advisor getAdvisor(LogTrace logTrace) {
		//포인트컷
		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		pointcut.setMappedNames("request*", "order*", "save*");

		//어드바이스
		LogTraceAdvice advice = new LogTraceAdvice(logTrace);
		return new DefaultPointcutAdvisor(pointcut, advice);
	}
}
