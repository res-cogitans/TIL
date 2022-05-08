package rescogitans.jpabook.chap07.part30compositekey.identifying.idclass;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class IdentifyingIdClassChildId implements Serializable {

    private String parent;
    private String ChildId;
}