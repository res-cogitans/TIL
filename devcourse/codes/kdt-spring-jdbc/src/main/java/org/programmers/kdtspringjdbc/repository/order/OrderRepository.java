package org.programmers.kdtspringjdbc.repository.order;

import org.programmers.kdtspringjdbc.order.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    public Optional<Order> findById(UUID orderId);
    public Order insert(Order order);
}
