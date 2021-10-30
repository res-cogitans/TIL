package cogitans.jpa_jpql.example;

import cogitans.jpa_jpql.MemberDTO;
import cogitans.jpa_jpql.domain.Address;
import cogitans.jpa_jpql.domain.Member;
import cogitans.jpa_jpql.domain.Team;

import javax.persistence.EntityManager;
import java.util.List;

public class Projection {

    public static void entityProjection(EntityManager em) {
        Member member = new Member();
        member.setUsername("member4");
        member.setAge(27);
        em.persist(member);

        em.flush();
        em.clear();

        List<Member> result = em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();

        Member foundMember = result.get(1);
        foundMember.setAge(50);
    }

    public static void entityProjectionWithAssociation(EntityManager em) {
//        List<Team> teamList = em.createQuery("SELECT  m.team FROM Member m", Team.class).getResultList();
        List<Team> teamList = em.createQuery("SELECT t FROM Member m join m.team t", Team.class).getResultList();

    }

    public static void embeddedTypeProjection(EntityManager em) {
        List<Address> result = em.createQuery("select o.address from Order o", Address.class).getResultList();
    }

    public static void scalaTypeProjection(EntityManager em) {
        List resultList = em.createQuery("SELECT DISTINCT m.username, m.age FROM Member m").getResultList();
    }
    public static void multipleValueProjection(EntityManager em) {
//        List result = em.createQuery("SELECT DISTINCT m.username, m.age FROM Member m").getResultList();
//
//        Object o1 = result.get(0);   // username
//        Object o2 = result.get(1);   // age

//         List<Object[]> result = em.createQuery("SELECT DISTINCT m.username, m.age FROM Member m").getResultList();

        List<MemberDTO> resultList = em.createQuery(
                "SELECT new cogitans.jpa_jpql.MemberDTO(m.username, m.age) FROM Member m", MemberDTO.class)
                .getResultList();

    }
}
