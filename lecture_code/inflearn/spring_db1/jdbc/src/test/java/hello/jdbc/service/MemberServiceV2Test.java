package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import hello.jdbc.repository.MemberRepositoryV2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 트랜잭션 - 커넥션 파라미터 전달 방식 동기화
 */
class MemberServiceV2Test {

    static final String MEMBER_A = "memberA";
    static final String MEMBER_B = "memberB";
    static final String MEMBER_EX = "ex";

    private MemberRepositoryV2 memberRepository;
    private MemberServiceV2 memberService;

    @BeforeEach
    void beforeEach() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepository = new MemberRepositoryV2(dataSource);
        memberService = new MemberServiceV2(dataSource, memberRepository);
    }

    @AfterEach
    void afterEach() throws SQLException {
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(MEMBER_EX);
    }

    @Test
    @DisplayName("정상 이체")
    public void accountTransferTest() throws SQLException {
        //Given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //When
        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        //Then
        Member foundMemberA = memberRepository.findById(memberA.getMemberId());
        Member foundMemberB = memberRepository.findById(memberB.getMemberId());
        assertThat(foundMemberA.getMoney()).isEqualTo(8000);
        assertThat(foundMemberB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체 중 예외 발생")
    public void accountTransferExTest() throws SQLException {
        //Given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberEx = new Member(MEMBER_EX, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberEx);

        //When
        assertAll(
                () -> assertThatThrownBy(
                        () -> memberService.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 2000))
                        .isInstanceOf(IllegalStateException.class),
                () -> assertThat(memberA.getMoney()).isEqualTo(10000),
                () -> assertThat(memberEx.getMoney()).isEqualTo(10000));
    }
}