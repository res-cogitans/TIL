package org.programmers.kdtspringorder.repository;

import org.programmers.kdtspringorder.order.Order;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryOrderRepository implements OrderRepository {

    private final Map<UUID, Order> storage = new ConcurrentHashMap<>();


    @Override
    public Optional<Order> findById(UUID orderId) {
        return Optional.ofNullable(storage.get(orderId));
    }

    @Override
    public Order insert(Order order) {
        storage.put(order.getOrderId(), order);
        return order;
    }
}
