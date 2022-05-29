package rescogitans.jpabook;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import rescogitans.jpabook.active.Member;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

//@SpringBootApplication(scanBasePackages = "rescogitans.jpabook.active")
public class JpaMain {

    public static void main(String[] args) {
        //엔티티 매니저 팩토리 생성
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("jpabook");
        //엔티티 매니저 생성
        EntityManager em = emf.createEntityManager();
        //트랜잭션 획득
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();        //트랜잭션 시작

            startingCriteria(em);

        } catch (Exception e) {
            tx.rollback();    //트랜잭션 롤백
        } finally {
            em.close();        //엔티티 매니저 종료
        }
        emf.close();        //엔티티 매니저 팩토리 종료
    }

    static void startingCriteria(EntityManager em) {
        //JPQL: SELECT m FROM Member m

        CriteriaBuilder cb = em.getCriteriaBuilder();  //Criteria 쿼리 빌더

        //Criteria 생성, 반환 타입 지정
        CriteriaQuery<Member> cq = cb.createQuery(Member.class);

        Root<Member> m = cq.from(Member.class); //FROM 절
        cq.select(m);   //SELECT 절

        TypedQuery<Member> query = em.createQuery(cq);
        List<Member> members = query.getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
        }
    }

}