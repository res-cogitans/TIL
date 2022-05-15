package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberRepository;

    @Test
    void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member foundMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(foundMember.getId()).isEqualTo(savedMember.getId());
    }

    @Test
    void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검색
        Member foundMember1 = memberRepository.findById(member1.getId()).get();
        Member foundMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(foundMember1).isEqualTo(member1);
        assertThat(foundMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long countAfterDelete = memberRepository.count();
        assertThat(countAfterDelete).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThan() {
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberA", 15);
        Member member3 = new Member("memberB", 25);
        Member member4 = new Member("memberA", 45);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        List<Member> foundResult = memberRepository.findByUsernameAndAgeGreaterThan("memberA", 14);
        assertThat(foundResult.size()).isEqualTo(2);

        for (Member member : foundResult) {
            assertThat(member.getUsername()).isEqualTo("memberA");
            assertThat(member.getAge()).isGreaterThan(14);
        }
    }

    @Test
    void testNamedQuery() {
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberA", 15);
        Member member3 = new Member("memberB", 25);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<Member> result = memberRepository.findByUsername("memberA");

        for (Member member : result) {
            assertThat(member).isIn(member1, member2);
        }
    }

    @Test
    void paging() {
        //Given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));
        memberRepository.save(new Member("member7", 10));
        memberRepository.save(new Member("member8", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        //When
        List<Member> members = memberRepository.findByPage(age, offset, limit);
        long totalCount = memberRepository.totalCount(age);

        //페이지 계산 공식 적용...
        // totalPage = totalCount / size ...
        // 마지막 페이지 ...
        // 최초 페이지 ...

        //Then
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(8);
    }

    @Test
    void bulkUpdate() {
        //Given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 15));
        memberRepository.save(new Member("member3", 22));
        memberRepository.save(new Member("member4", 26));
        memberRepository.save(new Member("member5", 34));
        memberRepository.save(new Member("member6", 19));
        memberRepository.save(new Member("member7", 46));
        memberRepository.save(new Member("member8", 43));

        //When
        int resultCount = memberRepository.bulkAgePlus(20);

        //Then
        assertThat(resultCount).isEqualTo(5);
    }
}