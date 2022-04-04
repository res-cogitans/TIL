package org.programmers.kdtspringorder;

import org.programmers.kdtspringorder.voucher.FixedAmountVoucher;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderTester {
    public static void main(String[] args) {
        var customerId = UUID.randomUUID();
        List<OrderItem> orderItems = new ArrayList<>() {
            {
                add(new OrderItem(UUID.randomUUID(), 100L, 1));
            }
        };
        var order = new Order(UUID.randomUUID(), customerId, orderItems,
                new FixedAmountVoucher(UUID.randomUUID(), 10L));
        Assert.isTrue(order.totalAmount() == 90L,
                MessageFormat.format("totalAmount {0} is not 90L", order.totalAmount()));
    }
}
