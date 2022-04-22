package rescogitans.jpabook;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChapterSix {

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

            oneToManySave(em);        //테스트 로직 실행

            tx.commit();    //트랜잭션 커밋

        } catch (Exception e) {
            tx.rollback();    //트랜잭션 롤백
        } finally {
            em.close();        //엔티티 매니저 종료
        }
        emf.close();        //엔티티 매니저 팩토리 종료
    }

    private void oneToManySave(EntityManager em) {
        Member member1 = new Member();
        member1.setUsername("testMemberA");
        Member member2 = new Member();
        member2.setUsername("testMemberB");

        Team team = new Team();
        team.setName("OneToManyUni");
        team.getMembers().add(member1);
        team.getMembers().add(member2);

        em.persist(member1);
        em.persist(member2);
        em.persist(team);
    }
}