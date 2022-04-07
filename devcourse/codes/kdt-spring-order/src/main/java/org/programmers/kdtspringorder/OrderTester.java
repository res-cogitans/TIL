package org.programmers.kdtspringorder;

import org.programmers.kdtspringorder.order.Order;
import org.programmers.kdtspringorder.order.OrderItem;
import org.programmers.kdtspringorder.repository.VoucherRepository;
import org.programmers.kdtspringorder.service.OrderService;
import org.programmers.kdtspringorder.service.VoucherService;
import org.programmers.kdtspringorder.voucher.FixedAmountVoucher;
import org.programmers.kdtspringorder.voucher.Voucher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderTester {
    public static void main(String[] args) {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfiguration.class);

        OrderService orderService = ac.getBean(OrderService.class);
        VoucherService voucherService = ac.getBean(VoucherService.class);
        VoucherRepository voucherRepository = ac.getBean(VoucherRepository.class);
        Voucher voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));

        List<OrderItem> orderItems = new ArrayList<>() {
            {
                add(new OrderItem(UUID.randomUUID(), 100L, 1));
            }
        };
        var customerId = UUID.randomUUID();
        Order order = orderService.createOrder(customerId, orderItems, voucher.getVoucherId());

        Assert.isTrue(order.totalAmount() == 90L,
                MessageFormat.format("totalAmount {0} is not 90L", order.totalAmount()));

    }
}
