package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberDataJpaRepository extends JpaRepository<Member, Long> {
}
