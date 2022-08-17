package hello.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import hello.proxy.config.v1_proxy.InterfaceProxyConfig;
import hello.proxy.config.v1_proxy.ConcreteProxyConfig;
import hello.proxy.config.v2_dynamicproxy.DynamicProxyBasicConfig;
import hello.proxy.config.v2_dynamicproxy.DynamicProxyFilterConfig;
import hello.proxy.config.v3_proxyfactory.ProxyFactoryConfigV1;
import hello.proxy.config.v3_proxyfactory.ProxyFactoryConfigV2;
import hello.proxy.trace.logtrace.LogTrace;
import hello.proxy.trace.logtrace.ThreadLocalLogTrace;

// @Import({AppV1Config.class, AppV2Config.class})
// @Import({InterfaceProxyConfig.class, ConcreteProxyConfig.class})
// @Import({DynamicProxyFilterConfig.class, ConcreteProxyConfig.class})
@Import({ProxyFactoryConfigV1.class, ProxyFactoryConfigV2.class})
@SpringBootApplication(scanBasePackages = "hello.proxy.app") //주의
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

	@Bean
	public LogTrace logTrace() {
		return new ThreadLocalLogTrace();
	}
}
