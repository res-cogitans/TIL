package rescogitans.jpabook.chap07.part30compositekey.identifying.embeddedid;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EbdParent {

    @Id
    @Column(name = "PARENT_ID")
    private String id;

    private String name;
}
