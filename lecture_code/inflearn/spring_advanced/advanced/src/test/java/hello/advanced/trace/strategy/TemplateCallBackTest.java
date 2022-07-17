package hello.advanced.trace.strategy;

import org.junit.jupiter.api.Test;

import hello.advanced.trace.strategy.code.template.TimeLogTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TemplateCallBackTest {

	@Test
	void callBack() {
		TimeLogTemplate template = new TimeLogTemplate();
		template.execute(() -> log.info("비즈니스 로직1 실행"));
		template.execute(() -> log.info("비즈니스 로직2 실행"));
	}
}
