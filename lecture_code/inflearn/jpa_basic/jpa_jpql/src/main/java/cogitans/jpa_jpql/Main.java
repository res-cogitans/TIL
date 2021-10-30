package cogitans.jpa_jpql;

import cogitans.jpa_jpql.example.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlTestingPersistence");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
//            BasicGrammar.typeQueryAndResults(em);
//            Projection.entityProjection(em);
//            SampleGenerator.generate(em);
//            Join.leftOuterJoin(em);
//            Join.joinUsingOn(em);
//            JpqlTypeExpression.types(em);
//            Conditional.caseConditional(em);
//            JpqlFunction.customFunction(em);
//            FetchJoin.entityFetchJoin(em);
//            NamedQuery.namedQuery(em);
            BulkOperation.bulk(em);

            tx.commit();
        } catch(Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
