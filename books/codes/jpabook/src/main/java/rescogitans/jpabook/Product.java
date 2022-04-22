package rescogitans.jpabook;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Product {

    @Id @Column(name = "PRODUCT_ID")
    private Long id;

    private String name;
    
//  연결 엔티티 양방향 조회
//    @OneToMany(mappedBy = "product")
//    private List<MemberProduct> memberProducts;
    
/*  //@ManyToMany 사용하는 경우
    @ManyToMany(mappedBy = "products")
    private List<Member> members;
 */
}
