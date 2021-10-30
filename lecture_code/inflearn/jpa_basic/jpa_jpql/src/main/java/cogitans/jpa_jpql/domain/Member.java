package cogitans.jpa_jpql.domain;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@NamedQuery(
        name = "Member.findByUsername",
        query = "SELECT m FROM Member m WHERE m.username = :username"
)
public class Member extends BaseEntity{

    private String username;
    private Integer age;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

//    @OneToMany(mappedBy = "member")
//    private List<Order> order = new ArrayList<>();

//    @Enumerated(EnumType.STRING)
//    private MemberType type;

    public void changeTeam(Team team) {
        this.team= team;
        team.getMembers().add(this);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id" + getId() +
                " username='" + username + '\'' +
                ", age=" + age +
                " , team=" + team +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public Integer getAge() {
        return age;
    }

    public Team getTeam() {
        return team;
    }

//    public List<Order> getOrder() {
//        return order;
//    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
