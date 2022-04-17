package rescogitans.jpabook;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MEMBER", uniqueConstraints = {
        @UniqueConstraint(
                name = "NAME_AGE_UNIQUE",
                columnNames = {"NAME", "AGE"}
        )})
@Getter
@Setter
public class Member {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    @Column(name = "NAME", nullable = false, length = 10)
    private String username;

    @ManyToOne
    @JoinColumn(name="TEAM_ID")
    private Team team;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob
    private String description;

    public void setTeam(Team team) {
        if (this.team != null) {
            this.team.getMembers().remove(this);
        }
        this.team = team;
        team.getMembers().add(this);
    }
}
