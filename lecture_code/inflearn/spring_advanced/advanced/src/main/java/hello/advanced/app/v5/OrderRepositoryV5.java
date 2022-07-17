package hello.advanced.app.v5;

import org.springframework.stereotype.Repository;

import hello.advanced.trace.callback.TraceTemplate;
import hello.advanced.trace.hellotrace.logtrace.LogTrace;

@Repository
public class OrderRepositoryV5 {

	private final LogTrace trace;
	private final TraceTemplate template;

	public OrderRepositoryV5(LogTrace trace) {
		this.trace = trace;
		this.template = new TraceTemplate(trace);
	}

	public void save(String itemId) {
		template.execute("orderRepository.save()", () -> {
			if (itemId.equals("ex")) {
				throw new IllegalStateException("예외 발생!");
			}
			sleep(1000);
			return null;
		});
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
