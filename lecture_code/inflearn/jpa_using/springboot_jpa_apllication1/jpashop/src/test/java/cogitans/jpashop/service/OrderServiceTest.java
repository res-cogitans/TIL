package cogitans.jpashop.service;

import cogitans.jpashop.domain.Address;
import cogitans.jpashop.domain.Member;
import cogitans.jpashop.domain.Order;
import cogitans.jpashop.domain.OrderStatus;
import cogitans.jpashop.domain.item.Book;
import cogitans.jpashop.domain.item.Item;
import cogitans.jpashop.exception.NotEnoughStockException;
import cogitans.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    @DisplayName("주문")
    public void order() {
        // given
        Member member = createMember();
        Item item = createBook("이광수", "무정", 10000, 10);

        int orderCount = 5;

        // when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // then
        Order foundOrder = orderRepository.findOne(orderId);

        assertThat(OrderStatus.ORDER).isEqualTo(foundOrder.getStatus());
        assertThat(1).isEqualTo(foundOrder.getOrderItems().size());
        assertThat(10000 * orderCount).isEqualTo(foundOrder.getTotalPrice());
        assertThat(5).isEqualTo(item.getStockQuantity());
    }

    @Test
    @DisplayName("주문 취소")
    public void cancelOrder() {
        //given
        Member member = createMember();
        Item item = createBook("헤겔", "정신현상학", 28000, 10);

        int orderCount = 4;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        assertThat(10).isEqualTo(item.getStockQuantity());
        assertThat(OrderStatus.CANCEL).isEqualTo(orderRepository.findOne(orderId).getStatus());
    }

    @Test
    @DisplayName("재고 수량을 초과한 주문")
    public void overOrder() {
        // given
        Member member = createMember();
        Item item = createBook("칸트", "순수이성비판", 33000, 3);

        int orderCount = 11;

        // when
        assertThrows(NotEnoughStockException.class, () ->
                orderService.order(member.getId(), item.getId(), orderCount));

        // then
    }

    private Book createBook(String author, String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setAuthor(author);
        book.setName(name);
        book.setIsbn("2455_2432");
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "아차산로", "123-456"));
        em.persist(member);
        return member;
    }

}