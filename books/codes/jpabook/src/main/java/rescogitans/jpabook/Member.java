package rescogitans.jpabook;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "NAME")
    private String username;

    private Integer age;
}
