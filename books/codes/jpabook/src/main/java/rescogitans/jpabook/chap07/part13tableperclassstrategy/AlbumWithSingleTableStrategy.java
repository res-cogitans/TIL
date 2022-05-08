package rescogitans.jpabook.chap07.part13tableperclassstrategy;

import javax.persistence.Entity;

@Entity
public class AlbumWithSingleTableStrategy extends ItemWithTablePerClassStrategy {

    private String artist;
}
