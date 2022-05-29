package rescogitans.jpabook.chap07.part13tableperclassstrategy;

import javax.persistence.Entity;

@Entity
public class AlbumWithTablePerStrategy extends ItemWithTablePerClassStrategy {

    private String artist;
}
