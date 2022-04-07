package org.programmers.kdtspringorder.order;

import org.programmers.kdtspringorder.voucher.Voucher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Order {

    private final UUID orderId;
    private final UUID customerId;
    private final List<OrderItem> orderItems;
    private Optional<Voucher> voucher;
    private OrderStatus orderStatus;

    public Order(UUID orderId, UUID customerId, List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderItems = orderItems;
        this.voucher = Optional.empty();
    }

    public Order(UUID orderId, UUID customerId, List<OrderItem> orderItems, Voucher voucher) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderItems = orderItems;
        this.voucher = Optional.of(voucher);
    }

    public long totalAmount() {
        var beforeDiscount = orderItems.stream().map(v -> v.productPrice() * v.quantity())
                .reduce(0L, Long::sum);
        if (voucher.isPresent()) {
            return voucher.get().discount(beforeDiscount);
        }
        else {
            return beforeDiscount;
        }
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
