package rescogitans.jpabook.chap07.part20mappedsuperclass;

import javax.persistence.Entity;

@Entity
public class SubClassMember extends BaseEntity {

    private String email;
}