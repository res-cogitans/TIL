package cogitans.jpa_jpql.example;

import cogitans.jpa_jpql.domain.MemberType;

import javax.persistence.EntityManager;
import java.util.List;

public class JpqlTypeExpression {

    public static void types(EntityManager em) {
        String query = "SELECT m.username, 'She''s', true FROM Member m";
        List<Object[]> result = em.createQuery(query)
                .setFirstResult(0)
                .setMaxResults(10)
                .getResultList();

        for (Object[] objects : result) {
            System.out.println("objects[0] = " + objects[0]);
            System.out.println("objects[0] = " + objects[1]);
            System.out.println("objects[0] = " + objects[2]);
        }
    }

    public static void enumType(EntityManager em) {
//        String query = "SELECT m.username, 'HELLO', true FROM Member m" +
//                "WHERE m.type = cogitans.jpa_jpql.domain.MemberType.ADMIN";

        String query = "SELECT m.username, 'HELLO', true FROM Member m" +
                "WHERE m.type = :userType";

        List<Object[]> result = em.createQuery(query)
                .setParameter("userType", MemberType.ADMIN)
                .setFirstResult(0)
                .setMaxResults(10)
                .getResultList();

        for (Object[] objects : result) {
            System.out.println("objects[0] = " + objects[0]);
            System.out.println("objects[0] = " + objects[1]);
            System.out.println("objects[0] = " + objects[2]);
        }
    }

}


