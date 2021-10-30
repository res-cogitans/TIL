package cogitans.jpa_jpql.example;

import javax.persistence.EntityManager;
import java.util.List;

public class Conditional {

    public static void caseConditional(EntityManager em) {
        String query =
                "SELECT " +
                        "CASE WHEN m.age <= 10 THEN '학생요금' " +
                        "     WHEN m.age >= 60 THEN '경로요금' " +
                        "     ELSE '일반요금' " +
                        "END " +
                "FROM Member m";
        List<String> resultList = em.createQuery(query, String.class)
                .getResultList();

        for (String s : resultList) {
            System.out.println("s = " + s);
        }
    }

    public static void coalesceConditional(EntityManager em) {
        String query = "SELECT COALESCE(m.username, '이름 없는 회원' AS username FROM Member m";
        List<String> resultList = em.createQuery(query, String.class)
                .getResultList();

        for (String s : resultList) {
            System.out.println("s = " + s);
        }
    }

    public static void nullifConditional(EntityManager em) {
        String query = "SELECT NULLIF(m.username, '관리자') AS username FROM Member m";
        List<String> resultList = em.createQuery(query, String.class)
                .getResultList();

        for (String s : resultList) {
            System.out.println("s = " + s);
        }


    }
}
