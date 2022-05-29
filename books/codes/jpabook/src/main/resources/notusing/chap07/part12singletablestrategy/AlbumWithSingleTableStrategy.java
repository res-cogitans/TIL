package rescogitans.jpabook.chap07.part12singletablestrategy;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")
public class AlbumWithSingleTableStrategy extends ItemWithSingleTableStrategy {

    private String artist;
}
