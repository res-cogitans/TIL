package hello.proxy.config.v3_proxyfactory;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ProxyFactoryConfigV2 {

	@Bean
	public OrderControllerV2 orderControllerV2(LogTrace logTrace) {
		OrderControllerV2 orderController = new OrderControllerV2(orderServiceV2(logTrace));

		ProxyFactory proxyFactory = new ProxyFactory(orderController);
		proxyFactory.addAdvisor(getAdvisor(logTrace));

		return (OrderControllerV2)proxyFactory.getProxy();
	}

	@Bean
	public OrderServiceV2 orderServiceV2(LogTrace logTrace) {
		OrderServiceV2 orderService = new OrderServiceV2(orderRepositoryV2(logTrace));

		ProxyFactory proxyFactory = new ProxyFactory(orderService);
		proxyFactory.addAdvisor(getAdvisor(logTrace));

		return (OrderServiceV2)proxyFactory.getProxy();
	}

	@Bean
	public OrderRepositoryV2 orderRepositoryV2(LogTrace logTrace) {
		OrderRepositoryV2 orderRepository = new OrderRepositoryV2();

		ProxyFactory proxyFactory = new ProxyFactory(orderRepository);
		proxyFactory.addAdvisor(getAdvisor(logTrace));

		return (OrderRepositoryV2)proxyFactory.getProxy();
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
