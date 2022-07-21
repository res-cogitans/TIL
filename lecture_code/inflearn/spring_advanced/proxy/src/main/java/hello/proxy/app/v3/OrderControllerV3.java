package hello.proxy.app.v3;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderControllerV3 {

	private final OrderServiceV3 orderServiceV3;

	public OrderControllerV3(OrderServiceV3 orderService) {
		this.orderServiceV3 = orderService;
	}

	@GetMapping("/v3/request")
	public String request(String itemId) {
		orderServiceV3.orderItem(itemId);
		return "ok";
	}

	@GetMapping("/v3/no-log")
	public String noLog() {
		return "ok";
	}
}
