package cogitans.jpashop.repository;

import cogitans.jpashop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {

    private String memberName;  //회원명
    private OrderStatus orderStatus; // 주문 상태[ORDER, CANCEL]
}
