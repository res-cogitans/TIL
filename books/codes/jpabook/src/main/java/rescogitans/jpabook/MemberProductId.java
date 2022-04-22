package rescogitans.jpabook;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class MemberProductId implements Serializable {

    private String member;
    private String product;
}
