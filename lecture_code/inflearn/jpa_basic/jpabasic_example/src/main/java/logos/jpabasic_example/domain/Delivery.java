package logos.jpabasic_example.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
public class Delivery {

    @Id @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    private String city;
    private String zipcode;
    private DeliveryStatus status;
}
