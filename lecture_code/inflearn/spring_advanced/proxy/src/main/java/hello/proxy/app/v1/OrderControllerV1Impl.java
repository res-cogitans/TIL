package hello.proxy.app.v1;

public class OrderControllerV1Impl implements OrderControllerV1{

	private final OrderServiceV1 orderServiceV1;

	public OrderControllerV1Impl(OrderServiceV1 orderService) {
		this.orderServiceV1 = orderService;
	}

	@Override
	public String request(String itemId) {
		orderServiceV1.orderItem(itemId);
		return "ok";
	}

	@Override
	public String noLog() {
		return "ok";
	}
}
