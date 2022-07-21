package hello.proxy.pureproxy.decorator;

import org.junit.jupiter.api.Test;

import hello.proxy.pureproxy.decorator.code.DecoratorPatternClient;
import hello.proxy.pureproxy.decorator.code.MessageDecorator;
import hello.proxy.pureproxy.decorator.code.RealComponent;
import hello.proxy.pureproxy.decorator.code.TimeDecorator;

public class DecoratorPatternTest {

	@Test
	void noDecorator() {
		DecoratorPatternClient client = new DecoratorPatternClient(new RealComponent());
		client.execute();
	}

	@Test
	void decorator1() {
		DecoratorPatternClient client = new DecoratorPatternClient(new MessageDecorator(new RealComponent()));
		client.execute();
	}

	@Test
	void decorator2() {
		DecoratorPatternClient client = new DecoratorPatternClient(new TimeDecorator(new MessageDecorator(new RealComponent())));
		client.execute();
	}
}
