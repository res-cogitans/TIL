package rescogitans.jpabook.chap07.part30compositekey.identifying.embeddedid;

import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class EbdGrandChildId implements Serializable {

    private EbdChildId childId;

    @Column(name = "GRANDCHILD_ID")
    private String id;
}
