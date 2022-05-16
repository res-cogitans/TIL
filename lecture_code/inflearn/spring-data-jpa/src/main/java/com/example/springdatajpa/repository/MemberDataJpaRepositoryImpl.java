package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class MemberDataJpaRepositoryImpl implements CustomMemberRepository {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("SELECT m FROM Member m")
                .getResultList();
    }
}
