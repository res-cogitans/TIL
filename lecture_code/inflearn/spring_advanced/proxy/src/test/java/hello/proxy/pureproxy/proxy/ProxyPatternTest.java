package hello.proxy.pureproxy.proxy;

import org.junit.jupiter.api.Test;

import hello.proxy.pureproxy.proxy.code.CacheProxy;
import hello.proxy.pureproxy.proxy.code.ProxyPatternClient;
import hello.proxy.pureproxy.proxy.code.RealSubject;

public class ProxyPatternTest {

	@Test
	void noProxyTest() {
		ProxyPatternClient client = new ProxyPatternClient(new RealSubject());
		client.execute();
		client.execute();
		client.execute();
	}

	@Test
	void cacheProxyTest() {
		ProxyPatternClient client = new ProxyPatternClient(new CacheProxy(new RealSubject()));
		client.execute();
		client.execute();
		client.execute();
	}
}
