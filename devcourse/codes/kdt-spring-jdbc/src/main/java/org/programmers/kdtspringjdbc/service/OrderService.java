package org.programmers.kdtspringjdbc.service;

import lombok.RequiredArgsConstructor;
import org.programmers.kdtspringjdbc.order.Order;
import org.programmers.kdtspringjdbc.order.OrderItem;
import org.programmers.kdtspringjdbc.repository.order.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final VoucherService voucherService;
    private final OrderRepository orderRepository;

    public Order createOrder(UUID customerId, List<OrderItem> orderItems) {
        var order = new Order(UUID.randomUUID(), customerId, orderItems);
        orderRepository.insert(order);
        voucherService.useVoucher();
        return order;
    }

    public Order createOrder(UUID customerId, List<OrderItem> orderItems, UUID voucherId) {
        var voucher = voucherService.getVoucher(voucherId);
        var order = new Order(UUID.randomUUID(), customerId, orderItems, voucher);
        orderRepository.insert(order);
        voucherService.useVoucher();
        return order;
    }

}
