package cogitans.jpa_jpql.example;

import javax.persistence.EntityManager;
import java.util.List;

public class JpqlFunction {

    public static void basicFunction(EntityManager em) {
//        String concatQuery = "SELECT 'a' || 'b' FROM Member m";
        String concatQuery = "SELECT CONCAT('a', 'b') FROM Member m";
        List<String> concatResult = em.createQuery(concatQuery, String.class).getResultList();

        for (String s : concatResult) {
            System.out.println("s = " + s);
        }

        String substringQuery = "SELECT SUBSTRING(m.username, 2, 3) FROM Member m";
        List<String> substringResult = em.createQuery(substringQuery, String.class).getResultList();

        for (String s : substringResult) {
            System.out.println("s = " + s);
        }

        String locateQuery = "SELECT LOCATE('de', 'abcdefg') FROM Member m";
        List<Integer> locateResult = em.createQuery(locateQuery, Integer.class).getResultList();

        for (Integer i : locateResult) {
            System.out.println("i = " + i);
        }

        String sizeQuery = "SELECT SIZE(t.members) FROM Member m";
        List<Integer> sizeResult = em.createQuery(sizeQuery, Integer.class).getResultList();

        for (Integer i : sizeResult) {
            System.out.println("i = " + i);
        }

    }

    public static void customFunction(EntityManager em) {
//        String query = "SELECT group_concat(m.username) FROM Member m";
        String query = "SELECT FUNCTION('group_concat', m.username) FROM Member m";
        List<String> result = em.createQuery(query, String.class).getResultList();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }
}
