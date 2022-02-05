package cogitans.jpashop.repository;

import cogitans.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepositoryWithSpringDataJpa extends JpaRepository<Member, Long> {

    List<Member> findByName(String name);
}
