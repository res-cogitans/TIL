package rescogitans.jpabook.chap07.part30compositekey.identifying.embeddedid;

import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class EbdChildId implements Serializable {

    private String parentId;

    @Column(name = "CHILD_ID")
    private String id;
}
