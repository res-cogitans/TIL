package hello.advanced.app.v5;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.advanced.trace.callback.TraceCallBack;
import hello.advanced.trace.callback.TraceTemplate;
import hello.advanced.trace.hellotrace.logtrace.LogTrace;

@RestController
public class OrderControllerV5 {

	private final OrderServiceV5 orderService;
	private final TraceTemplate template;
	private final LogTrace trace;

	public OrderControllerV5(OrderServiceV5 orderService, LogTrace trace) {
		this.orderService = orderService;
		this.trace = trace;
		this.template = new TraceTemplate(trace);
	}

	@GetMapping("/v5/request")
	public String request(String itemId) {
		return template.execute("OrderController.request", () -> {
			orderService.orderItem(itemId);
			return "ok";
		});
	}
}
