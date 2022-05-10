package rescogitans.jpabook.chap07.part30compositekey.identifying.idclass;

import javax.persistence.*;

//@Entity
//@IdClass(IdentifyingIdClassGrandChildId.class)
public class IdentifyingIdClassGrandChild {

    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "PARENT_ID"),
            @JoinColumn(name = "CHILD_ID")
    })
    private IdentifyingIdClassChild child;

    @Id
    @Column(name = "GRANDCHILD_ID")
    private String id;
    
    private String name;
}
