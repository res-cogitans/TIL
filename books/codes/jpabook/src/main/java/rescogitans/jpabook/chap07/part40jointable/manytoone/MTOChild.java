package rescogitans.jpabook.chap07.part40jointable.manytoone;

import javax.persistence.*;

@Entity
public class MTOChild {

    @Id
    @GeneratedValue
    @Column(name = "CHILD_ID")
    private Long id;
    private String name;

    @ManyToOne(optional = false)
    @JoinTable(name = "PARENT_CHILD",
    joinColumns = @JoinColumn(name = "CHILD_ID"),
    inverseJoinColumns = @JoinColumn(name="PARENT_ID"))
    private MTOParent parent;
}
