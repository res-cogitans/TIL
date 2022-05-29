package rescogitans.jpabook.chap01to06;

import rescogitans.jpabook.active.Member;

import javax.persistence.*;

@Entity
public class Order {

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    private int OrderAmount;
}
