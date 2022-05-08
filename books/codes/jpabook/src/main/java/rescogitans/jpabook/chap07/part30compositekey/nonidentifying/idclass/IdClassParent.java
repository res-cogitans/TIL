package rescogitans.jpabook.chap07.part30compositekey.nonidentifying.idclass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(IdClassParentId.class)
public class IdClassParent {

    @Id @Column(name = "PARENT_ID1")
    private String id1;
    @Id @Column(name = "PARENT_ID2")
    private String id2;

    private String name;
}
