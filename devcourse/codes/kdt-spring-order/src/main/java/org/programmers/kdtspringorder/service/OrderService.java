package org.programmers.kdtspringorder.service;

import lombok.RequiredArgsConstructor;
import org.programmers.kdtspringorder.domain.Order;
import org.programmers.kdtspringorder.domain.OrderItem;
import org.programmers.kdtspringorder.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

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
