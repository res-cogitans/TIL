package rescogitans.jpabook.chap07.part30compositekey.nonidentifying.idclass;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class IdClassParentId implements Serializable {

    private String id1;
    private String id2;
}
