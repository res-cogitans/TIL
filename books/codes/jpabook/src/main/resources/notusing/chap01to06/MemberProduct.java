package rescogitans.jpabook.chap01to06;

import javax.persistence.*;

@Entity
@IdClass(MemberProductId.class)
public class MemberProduct {

//    @Id
//    @ManyToOne
//    @JoinColumn(name = "MEMBER_ID")
//    private Member member;

    @Id
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    private int orderAmount;
}
