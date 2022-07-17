package hello.advanced;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.advanced.trace.hellotrace.logtrace.LogTrace;
import hello.advanced.trace.hellotrace.logtrace.ThreadLocalLogTrace;

@Configuration
public class LogTraceConfig {

	@Bean
	public LogTrace logTrace() {
		return new ThreadLocalLogTrace();
	}
}
