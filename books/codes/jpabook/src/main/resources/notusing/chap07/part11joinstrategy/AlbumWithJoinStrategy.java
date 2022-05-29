package rescogitans.jpabook.chap07.part11joinstrategy;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")
public class AlbumWithJoinStrategy extends ItemWithJoinStrategy {

    private String artist;
}
