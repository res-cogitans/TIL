package rescogitans.jpabook.chap07.part30compositekey.identifying.embeddedid;

import javax.persistence.*;

@Entity
public class EbdGrandChild {

    @EmbeddedId
    private EbdGrandChildId id;

    @MapsId("childId")
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "PARENT_ID"),
            @JoinColumn(name = "CHILD_ID")
    })
    private EbdChild child;

    private String name;
}
