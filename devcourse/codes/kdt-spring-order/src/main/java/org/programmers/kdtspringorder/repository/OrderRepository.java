package org.programmers.kdtspringorder.repository;

import org.programmers.kdtspringorder.order.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    public Optional<Order> findById(UUID orderId);
    public Order insert(Order order);
}
