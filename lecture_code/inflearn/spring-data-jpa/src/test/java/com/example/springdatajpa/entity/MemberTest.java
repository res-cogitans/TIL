package com.example.springdatajpa.entity;

import com.example.springdatajpa.repository.MemberDataJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberDataJpaRepository memberRepository;

    @Test
    void testEntity() {
        Team teamA = new Team("TeamA");;
        Team teamB = new Team("TeamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        //초기화
        em.flush();
        em.clear();

        //확인
        List<Member> members = em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("-> member.getTeam() = " + member.getTeam());
        }
    }

    @Test
    void jpaEventBaseEntity() throws InterruptedException {
        //Given
        Member member = new Member("member1");
        memberRepository.save(member);

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush(); //@PreUpdate
        em.clear();

        //When
        Member foundMember = memberRepository.findById(member.getId()).get();

        //Then
        System.out.println("foundMember.getCreatedDate() = " + foundMember.getCreatedDate());
        System.out.println("foundMember.getUpdatedDate() = " + foundMember.getLastModifiedDate());
        System.out.println("foundMember.getCreatedBy() = " + foundMember.getCreatedBy());
        System.out.println("foundMember.getLastModifiedBy() = " + foundMember.getLastModifiedBy());
    }
}