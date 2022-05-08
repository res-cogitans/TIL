package org.programmers.kdtspringjdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.programmers.kdtspringjdbc.configuration.AppConfiguration;
import org.programmers.kdtspringjdbc.order.OrderItem;
import org.programmers.kdtspringjdbc.repository.order.OrderRepository;
import org.programmers.kdtspringjdbc.repository.voucher.VoucherRepository;
import org.programmers.kdtspringjdbc.service.OrderService;
import org.programmers.kdtspringjdbc.voucher.FixedAmountVoucher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)      //JUnit5: jupiter 관련, JUnit4의 경우 다른 걸 사용해야 함
@ContextConfiguration(classes = {AppConfiguration.class}) //xml, class 등의 설정 정보를 줘서 적용시킬 수 있다.
// 혹은 설정정보를 아래의 Config inner 클래스처럼 만들어 적용할 수도 있다.
//위의 두 Annotation을 아래 하나의 Annotation으로 대체할 수 있다.
//@SpringJUnitConfig
//@ActiveProfiles("test)    //profile 적용도 가능하다.
public class KdtSpringContextTests {

//    @Configuration
//    static class Config {
//        ...
//    }

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    VoucherRepository voucherRepository;


    @Test
    @DisplayName("테스트용 ApplicationContext 생성 확인")
    void testApplicationContext() {
        assertThat(applicationContext, notNullValue());
    }

    @Test
    @DisplayName("VoucherRepository Bean 등록 확인")
    void testVoucherRepositoryCreation() {
        VoucherRepository bean = applicationContext.getBean(VoucherRepository.class);
        assertThat(bean, notNullValue());
    }

    @Test
    @DisplayName("OrderService 사용하여 주문을 생성")
    void testOrderService() {
        //Given
        FixedAmountVoucher fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        voucherRepository.insert(fixedAmountVoucher);

        //When
        var order = orderService.createOrder(UUID.randomUUID(), List.of(
                new OrderItem(UUID.randomUUID(), 200, 1)), fixedAmountVoucher.getVoucherId());

        //Then
        assertThat(order.totalAmount(), is(100l));
        assertThat(order.getVoucher().isEmpty(), is(false));
        assertThat(order.getVoucher().get().getVoucherId(), is(fixedAmountVoucher.getVoucherId()));
    }
}
