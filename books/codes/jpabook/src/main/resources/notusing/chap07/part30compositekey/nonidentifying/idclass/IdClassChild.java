package rescogitans.jpabook.chap07.part30compositekey.nonidentifying.idclass;

import javax.persistence.*;

@Entity
public class IdClassChild {

    @Id
    private String id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "PARENT_ID1",
                    referencedColumnName = "PARENT_ID1"),
            @JoinColumn(name = "PARENT_ID2",
                    referencedColumnName = "PARENT_ID2")
    })
    private IdClassParent parent;
}
