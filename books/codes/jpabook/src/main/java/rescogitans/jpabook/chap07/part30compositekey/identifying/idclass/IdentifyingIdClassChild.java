package rescogitans.jpabook.chap07.part30compositekey.identifying.idclass;

import javax.persistence.*;

//@Entity
//@IdClass(IdentifyingIdClassChildId.class)
public class IdentifyingIdClassChild {

    @Id
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    public IdentifyingIdClassParent parent;

    @Id
    @Column(name = "CHILD_ID")
    private String childId;

    private String name;
}
