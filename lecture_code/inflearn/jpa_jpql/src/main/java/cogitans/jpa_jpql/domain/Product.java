package cogitans.jpa_jpql.domain;

import javax.persistence.Entity;

@Entity
public class Product extends BaseEntity {

    private String name;
    private Integer price;
    private Integer stockAmount;
}
