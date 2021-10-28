package cogitans.jpa_jpql.example;

import cogitans.jpa_jpql.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;

public class NamedQuery {

    public static void namedQuery(EntityManager em) {
        List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", "member1")
                .getResultList();

        for (Member member : resultList) {
            System.out.println("member = " + member);
        }
    }
}
