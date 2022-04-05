package org.programmers.kdtspringorder;

import org.programmers.kdtspringorder.domain.Order;
import org.programmers.kdtspringorder.repository.OrderRepository;
import org.programmers.kdtspringorder.repository.VoucherRepository;
import org.programmers.kdtspringorder.service.OrderService;
import org.programmers.kdtspringorder.service.VoucherService;
import org.programmers.kdtspringorder.voucher.Voucher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.UUID;

@Configuration
@ComponentScan
public class AppConfiguration {

    @Bean
    public VoucherRepository voucherRepository() {
        return new VoucherRepository() {
            @Override
            public Optional<Voucher> findById(UUID voucherId) {
                return Optional.empty();
            }
        };
    }

    @Bean
    public OrderRepository orderRepository() {
        return new OrderRepository() {
            @Override
            public void insert(Order order) {

            }
        };
    }

    @Bean
    public VoucherService voucherService(VoucherRepository voucherRepository) {
        return new VoucherService(voucherRepository);
    }

    @Bean
    public OrderService orderService(VoucherService voucherService, OrderRepository orderRepository) {
        return new OrderService(voucherService, orderRepository);
    }
}
