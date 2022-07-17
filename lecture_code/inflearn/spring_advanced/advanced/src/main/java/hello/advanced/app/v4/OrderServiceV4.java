package hello.advanced.app.v4;

import org.springframework.stereotype.Service;

import hello.advanced.trace.hellotrace.logtrace.LogTrace;
import hello.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceV4 {

	private final OrderRepositoryV4 orderRepository;
	private final LogTrace trace;

	public void orderItem(String orderId) {
		AbstractTemplate<Void> template = new AbstractTemplate<>(trace) {
			@Override
			protected Void call() {
				orderRepository.save(orderId);
				return null;
			}
		};
		template.execute("OrderService.orderItem()");
	}
}
