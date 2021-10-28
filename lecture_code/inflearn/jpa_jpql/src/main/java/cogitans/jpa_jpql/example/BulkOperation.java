package cogitans.jpa_jpql.example;

import javax.persistence.EntityManager;

public class BulkOperation {

    public static void bulk(EntityManager em) {
        int resultCount = em.createQuery("UPDATE Member m SET m.age = 20")
                .executeUpdate();

        System.out.println("resultCount = " + resultCount);
    }
}
