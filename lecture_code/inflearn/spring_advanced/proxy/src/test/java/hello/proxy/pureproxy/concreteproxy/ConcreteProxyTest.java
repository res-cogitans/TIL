package hello.proxy.pureproxy.concreteproxy;

import org.junit.jupiter.api.Test;

import hello.proxy.pureproxy.concreteproxy.code.ConcreteClient;
import hello.proxy.pureproxy.concreteproxy.code.ConcreteLogic;
import hello.proxy.pureproxy.concreteproxy.code.TimeProxy;

public class ConcreteProxyTest {

	@Test
	void noProxy() {
		ConcreteClient client = new ConcreteClient(new ConcreteLogic());
		client.execute();
	}

	@Test
	void addProxy() {
		ConcreteLogic concreteLogic = new ConcreteLogic();
		TimeProxy timeProxy = new TimeProxy(concreteLogic);
		ConcreteClient client = new ConcreteClient(timeProxy);
		client.execute();
	}
}
