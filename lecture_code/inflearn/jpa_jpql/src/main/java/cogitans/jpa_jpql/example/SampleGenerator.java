package cogitans.jpa_jpql.example;

import cogitans.jpa_jpql.domain.Member;
import cogitans.jpa_jpql.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Random;

public class SampleGenerator {
    public static void generate(EntityManager em) {
        for (int i = 0; i < 10; i++) {
            Team team = new Team();
            team.setName("team" + i);
            em.persist(team);
        }

        Random random = new Random();

        for (int i = 0; i < 100; i++) {

            Member member = new Member();
            member.setUsername("member" + i);
            member.setAge(random.nextInt(100));

            String query = "SELECT t FROM Team t WHERE t.name = 'team" + random.nextInt(10) + "'";
            Team foundTeam = em.createQuery(query, Team.class).getSingleResult();

            member.changeTeam(foundTeam);
            em.persist(member);
        }

    }
}
