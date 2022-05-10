package rescogitans.jpabook;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

@SpringBootTest
public class TestDetatched {

    @Test
    @Transactional
    void jpaTestTemplate() {
        //엔티티 매니저 팩토리 생성
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("jpabook");
        //엔티티 매니저 생성
        EntityManager em = emf.createEntityManager();
        //트랜잭션 획득
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();        //트랜잭션 시작

            // Given
            Customer customer = new Customer();
            customer.setId(1L);
            customer.setFirstName("honggu");
            customer.setLastName("kang");

            em.persist(customer);

            // When
            customer.setFirstName("guppy"); // 업데이트 쿼리 x

            // Then
            Assertions.assertThat(em.find(Customer.class, 1L).getFirstName()).isEqualTo(customer.getFirstName());

            tx.commit();    //트랜잭션 커밋

        } catch (Exception e) {
            tx.rollback();    //트랜잭션 롤백
        } finally {
            em.close();        //엔티티 매니저 종료
        }
        emf.close();        //엔티티 매니저 팩토리 종료
    }

    @Test
    void update() {
    }
}
