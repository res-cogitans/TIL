package org.programmers.kdtspringjdbc.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.programmers.kdtspringjdbc.order.Order;
import org.programmers.kdtspringjdbc.order.OrderItem;
import org.programmers.kdtspringjdbc.repository.voucher.MemoryVoucherRepository;
import org.programmers.kdtspringjdbc.repository.order.OrderRepository;
import org.programmers.kdtspringjdbc.voucher.FixedAmountVoucher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    class OrderRepositoryStub implements OrderRepository {

        @Override
        public Optional<Order> findById(UUID orderId) {
            return Optional.empty();
        }

        @Override
        public Order insert(Order order) {
            return null;
        }
    }

    @Test
    @DisplayName("stub 직접 만들어서 테스트해보기")
    void createOrder() {
        //Given
        var voucherRepository = new MemoryVoucherRepository();
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        voucherRepository.insert(fixedAmountVoucher);
        var voucherService = new VoucherService(voucherRepository);
        var sut = new OrderService(voucherService, new OrderRepositoryStub());

        //When
        var order = sut.createOrder(UUID.randomUUID(), List.of(
                new OrderItem(UUID.randomUUID(), 200, 1)), fixedAmountVoucher.getVoucherId());

        //Then
        assertThat(order.totalAmount(), is(100l));
        assertThat(order.getVoucher().isEmpty(), is(false));
        assertThat(order.getVoucher().get().getVoucherId(), is(fixedAmountVoucher.getVoucherId()));
    }

    @Test
    @DisplayName("mockito 사용한 테스트")
    void createOrderByMock() {
        //Given
        var voucherServiceMock = mock(VoucherService.class);
        var orderRepositoryMock = mock(OrderRepository.class);

        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        when(voucherServiceMock.getVoucher(fixedAmountVoucher.getVoucherId())).thenReturn(fixedAmountVoucher);

        var sut = new OrderService(voucherServiceMock, orderRepositoryMock);

        //When
        var order = sut.createOrder(UUID.randomUUID(), List.of(
                new OrderItem(UUID.randomUUID(), 200, 1)), fixedAmountVoucher.getVoucherId());

        //Then
        assertThat(order.totalAmount(), is((100L)));
        assertThat(order.getVoucher().isEmpty(), is(false));

        var inOrder = inOrder(voucherServiceMock);
        verify(voucherServiceMock).getVoucher(fixedAmountVoucher.getVoucherId());  //행위의 관점에서 메서드 호출 여부를 살핌
        verify(orderRepositoryMock).insert(order);
    }
}