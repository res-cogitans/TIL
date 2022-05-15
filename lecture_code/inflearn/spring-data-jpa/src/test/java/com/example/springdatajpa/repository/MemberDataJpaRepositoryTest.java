package com.example.springdatajpa.repository;

import com.example.springdatajpa.dto.MemberDto;
import com.example.springdatajpa.entity.Member;
import com.example.springdatajpa.entity.Team;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
@Rollback(false)
class MemberDataJpaRepositoryTest {

    @Autowired
    MemberDataJpaRepository memberRepository;
    @Autowired
    TeamDataJpaRepository teamRepository;
    @PersistenceContext
    EntityManager em;

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

        List<Member> result = memberRepository.findByNamedQuery("memberA");

        for (Member member : result) {
            assertThat(member).isIn(member1, member2);
        }
    }

    @Test
    void testQuery() {
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberA", 15);
        Member member3 = new Member("memberB", 25);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<Member> result = memberRepository.findUser("memberA", 10);

        assertThat(result.get(0)).isEqualTo(member1);
    }

    @Test
    void findUsernameList() {
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberB", 15);
        Member member3 = new Member("memberC", 25);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<String> usernameList = memberRepository.findUsernameList();
        assertThat(usernameList.size()).isEqualTo(3);

        for (String username : usernameList) {
            assertThat(username).isIn("memberA", "memberB", "memberC");
        }
    }

    @Test
    void findDto() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("memberA", 10, teamA);
        Member member2 = new Member("memberB", 15, teamB);
        Member member3 = new Member("memberC", 25, teamA);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<MemberDto> memberDtoList = memberRepository.findMemberDto();
        assertThat(memberDtoList.size()).isEqualTo(3);

        for (MemberDto memberDto: memberDtoList) {
            log.info("memberDto = {}", memberDto);
        }
    }

    @Test
    void findByNames() {
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberB", 15);
        Member member3 = new Member("memberC", 25);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<Member> result = memberRepository.findByNames(Arrays.asList("memberA", "memberC"));

        assertThat(result.size()).isEqualTo(2);

        for (Member member : result) {
            assertThat(member.getUsername()).isIn("memberA", "memberC");
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
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //When
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        Page<MemberDto> dtoPage = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //Then
        List<Member> members = page.getContent();
        long totalCount = page.getTotalElements();

        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(8);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(3);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
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

//        em.clear();

        Member foundMember5 = memberRepository.findMemberByUsername("member5");
        System.out.println("foundMember5 = " + foundMember5);

        //Then
        assertThat(resultCount).isEqualTo(5);
    }

    @Test
    void findMemberLazy() {
        //Given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 20, teamA);
        Member member2 = new Member("member2", 35, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //When
        List<Member> members = memberRepository.findAll();
//        List<Member> members = memberRepository.findMemberFetchJoin();

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member's team.getClass() = " + member.getTeam().getClass());
            System.out.println("member's team = " + member.getTeam().getName());
        }
    }

    @Test
    void queryHint() {
        //Given
        Member member1 = new Member("member1", 20);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //When
        Member foundMember = memberRepository.findReadOnlyByUsername(member1.getUsername());
        foundMember.setUsername("member2");
        em.flush();
    }

    @Test
    void lock() {
        //Given
        Member member1 = new Member("member1", 20);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //When
        List<Member> members = memberRepository.findLockByUsername(member1.getUsername());
        em.flush();
    }
}