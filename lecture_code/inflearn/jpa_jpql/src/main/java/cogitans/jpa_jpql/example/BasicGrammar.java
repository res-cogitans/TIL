package cogitans.jpa_jpql.example;

import cogitans.jpa_jpql.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class BasicGrammar {

    public static void typeQueryAndResults(EntityManager em) {
        Member member1 = new Member();
        member1.setUsername("member1");
        member1.setAge(16);
        em.persist(member1);

        Member member2 = new Member();
        member2.setUsername("member2");
        member2.setAge(24);
        em.persist(member2);

        Member member3 = new Member();
        member3.setUsername("member1");
        member3.setAge(64);
        em.persist(member3);



        // createQuery
        TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
        Query query2 = em.createQuery("select m.username, m.age from Member m");
        TypedQuery<String> query3 = em.createQuery("select m.username from Member m", String.class);

        query2.getResultList();

        // 파라미터 바인딩 - 이름 기준
        TypedQuery<Member> boundParameterQuery = em.createQuery("select m from Member m where m.username = :username", Member.class);
        boundParameterQuery.setParameter("username", "member2");
        Member boundParameterQuerySingleResult = boundParameterQuery.getSingleResult();
        System.out.println("=========================================================================");
        System.out.println("boundParameterQuerySingleResult.getName() = " + boundParameterQuerySingleResult.getUsername());
        System.out.println("=========================================================================");

        // 파라미터 바인딩 - 위치 기준
        List<Member> resultListByOrder = em.createQuery(
                "select m from Member m where m.username=?1", Member.class)
                .setParameter(1, "member1")
                .getResultList();
        System.out.println("=========================================================================");
        for (Member m : resultListByOrder) {
            System.out.println("m = " + m.getAge());
        }
        System.out.println("=========================================================================");


    }
}
