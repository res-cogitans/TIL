package cogitans.jpashop.api;

import cogitans.jpashop.domain.Address;
import cogitans.jpashop.domain.Order;
import cogitans.jpashop.domain.OrderStatus;
import cogitans.jpashop.repository.OrderRepository;
import cogitans.jpashop.repository.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 일차적으로는
 * X to One Case 다룬다. (컬렉션이 아닌 경우)
 * Order
 * Order -> Member (ManyToOne)
 * Order -> Delivery (OneToOne)
 *
 * // Order -> OrderItem (OneToMany)
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public Result ordersV2() {
        return new Result(
                orderRepository.findAllByString(new OrderSearch())
                        .stream()
                        .map(o -> new SimpleOrderDto(o))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/api/v3/simple-orders")
    public Result ordersV3() {
        return new Result(
                orderRepository.findAllWithMemberAndDelivery()
                        .stream()
                        .map(o -> new SimpleOrderDto(o))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/api/v4/simple-orders")
    public Result ordersV4() {
        return new Result(orderRepository.findOrderDtos());
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
