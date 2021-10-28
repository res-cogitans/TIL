package cogitans.jpa_jpql.example;

import cogitans.jpa_jpql.domain.Member;
import cogitans.jpa_jpql.domain.Team;

import javax.persistence.EntityManager;
import java.util.List;

public class FetchJoin {

    public static void entityFetchJoin(EntityManager em) {
        String query = "SELECT m FROM Member m JOIN FETCH m.team";
        List<Member> resultList = em.createQuery(query, Member.class)
                .getResultList();

        for (Member member : resultList) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }
    public static void collectionFetchJoin(EntityManager em) {
        String query = "SELECT t FROM Team t JOIN FETCH t.members";
        List<Team> resultList = em.createQuery(query, Team.class)
                .getResultList();

        for (Team team : resultList) {
            System.out.println("team = " + team);
            System.out.println("team.getMembers() = " + team.getMembers());
        }
    }
}
