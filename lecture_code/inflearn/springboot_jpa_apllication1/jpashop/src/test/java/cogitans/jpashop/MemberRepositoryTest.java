package cogitans.jpashop;

import cogitans.jpashop.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    @DisplayName("회원 저장 및 조회")
    void testMember() {
        //given
        Member member = new Member();
        member.setName("memberA");

        //when
        Long savedId = memberRepository.save(member);
        Member foundMember = memberRepository.find(member.getId());

        //then
        assertThat(foundMember.getId()).isEqualTo(member.getId());
        assertThat(foundMember.getName()).isEqualTo(member.getName());
        assertThat(foundMember).isEqualTo(member);
    }
}