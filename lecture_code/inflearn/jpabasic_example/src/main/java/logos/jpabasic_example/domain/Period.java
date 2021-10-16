package logos.jpabasic_example.domain;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class Period {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public boolean isWork() {
        return true;
    }

    public Period() {
    }
}
