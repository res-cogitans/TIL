package hello.advanced.app.v5;

import org.springframework.stereotype.Service;

import hello.advanced.trace.callback.TraceTemplate;
import hello.advanced.trace.hellotrace.logtrace.LogTrace;

@Service
public class OrderServiceV5 {

	private final OrderRepositoryV5 orderRepository;
	private final LogTrace trace;
	private final TraceTemplate template;

	public OrderServiceV5(OrderRepositoryV5 orderRepository, LogTrace trace) {
		this.orderRepository = orderRepository;
		this.trace = trace;
		this.template = new TraceTemplate(trace);
	}

	public void orderItem(String orderId) {
		template.execute("OrderService.orderItem()", () -> {
			orderRepository.save(orderId);
			return null;
		});
	}
}
