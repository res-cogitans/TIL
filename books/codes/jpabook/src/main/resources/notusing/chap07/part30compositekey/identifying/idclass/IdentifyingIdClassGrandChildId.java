package rescogitans.jpabook.chap07.part30compositekey.identifying.idclass;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class IdentifyingIdClassGrandChildId implements Serializable {

    private IdentifyingIdClassChildId child;
    private String id;
}
