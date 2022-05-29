package rescogitans.jpabook.chap07.part30compositekey.identifying.embeddedid;

import javax.persistence.*;

@Entity
public class EbdChild {

    @EmbeddedId
    private EbdChildId id;

    @MapsId("parentId")
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private EbdParent parent;

    private String name;
}
