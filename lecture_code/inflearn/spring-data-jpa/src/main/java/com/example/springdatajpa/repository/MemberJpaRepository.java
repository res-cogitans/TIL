package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public List<Member> findAll() { //JPQL 사용
        return em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public long count() {
        return em.createQuery("SELECT COUNT(m) FROM Member m", Long.class)
                .getSingleResult();
    }
}
