package cogitans.jpa_jpql.example;

import cogitans.jpa_jpql.domain.Member;
import cogitans.jpa_jpql.domain.Team;

import javax.persistence.EntityManager;
import java.util.List;

public class Join {

    public static void innerJoin(EntityManager em) {
        List<Member> result = em.createQuery("SELECT m FROM Member m JOIN m.team t", Member.class).getResultList();
    }
    public static void leftOuterJoin(EntityManager em) {
        List<Member> result = em.createQuery(
                "SELECT m FROM Member m LEFT OUTER JOIN m.team t ORDER BY m.age DESC", Member.class)
                .setFirstResult(5)
                .setMaxResults(20)
                .getResultList();

        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    public static void thetaJoin(EntityManager em) {
        List<Member> result = em.createQuery(
                "SELECT m FROM Member m, Team t ORDER BY m.age DESC", Member.class)
                .setFirstResult(5)
                .setMaxResults(20)
                .getResultList();

        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    public static void joinUsingOn(EntityManager em) {
        List<Member> result = em.createQuery(
                "SELECT m FROM Member m LEFT JOIN m.team t ON t.name='team4'", Member.class)
                .getResultList();

        System.out.println("result.size() = " + result.size());
        
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }
}
