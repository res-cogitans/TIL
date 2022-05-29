package rescogitans.jpabook.chap07.part30compositekey.nonidentifying.embeddedid;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class EmbeddedIdParent {

    @EmbeddedId
    private EmbeddedParentId id;

    private String name;
}
