package hello.proxy;

import hello.proxy.config.v5_autoproxy.AutoProxyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import hello.proxy.trace.logtrace.LogTrace;
import hello.proxy.trace.logtrace.ThreadLocalLogTrace;

// @Import({AppV1Config.class, AppV2Config.class})
// @Import({InterfaceProxyConfig.class, ConcreteProxyConfig.class})
// @Import({DynamicProxyFilterConfig.class, ConcreteProxyConfig.class})
//@Import({ProxyFactoryConfigV1.class, ProxyFactoryConfigV2.class})
@Import(AutoProxyConfig.class)
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
