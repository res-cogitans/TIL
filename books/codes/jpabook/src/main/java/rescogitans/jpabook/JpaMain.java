package rescogitans.jpabook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

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
            testDetach(em);        //비즈니스 로직 실행
            tx.commit();    //트랜잭션 커밋

        } catch (Exception e) {
            tx.rollback();    //트랜잭션 롤백
        } finally {
            em.close();        //엔티티 매니저 종료
        }
        emf.close();        //엔티티 매니저 팩토리 종료
    }

    private static void testDetach(EntityManager em) {
        // Given
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("honggu");
        customer.setLastName("kang");

        em.persist(customer);
        System.out.println("persist==============================================");
        // When
        customer.setFirstName("guppy"); // 업데이트 쿼리 x

        System.out.println("customer.getFirstName() = " + customer.getFirstName());

        Customer foundCustomer = em.find(Customer.class, 1L);
        System.out.println("foundCustomer.getFirstName() = " + foundCustomer.getFirstName());
    }

    private static void logic(EntityManager em) {
        Member member = new Member();
        member.setUsername("tester");
//        member.setAge(25);

        //등록
        em.persist(member);

        //수정
//        member.setAge(40);

        //단건 조회
        Member foundMember = em.find(Member.class, member.getId());
        System.out.println("foundMember.getId() = " + foundMember.getId());
        System.out.println("foundMember.getUsername() = " + foundMember.getUsername());
//        System.out.println("foundMember.getAge() = " + foundMember.getAge());

		//목록 조회
        List<Member> members = em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();
		System.out.println("members.size() = " + members.size());

		//삭제
		em.remove(member);
    }

}
