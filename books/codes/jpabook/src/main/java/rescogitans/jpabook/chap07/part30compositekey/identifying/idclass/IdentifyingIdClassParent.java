package rescogitans.jpabook.chap07.part30compositekey.identifying.idclass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class IdentifyingIdClassParent {

    @Id @Column(name = "PARENT_ID")
    private String id;
    private String name;
}
