package cogitans.jpa_jpql.example;

import cogitans.jpa_jpql.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;

public class Paging {

    public static void pagingAndPrintResult(EntityManager em) {

        List<Member> result = em.createQuery("SELECT m FROM Member m ORDER BY m.age DESC", Member.class)
                .setFirstResult(0)
                .setMaxResults(15)
                .getResultList();

        System.out.println("==============================================");
        System.out.println("result.size() = " + result.size());
        System.out.println("**********************************************");
        for (Member m : result) {
            System.out.println("member = " + m);
        }
        System.out.println("==============================================");

    }
}
