package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamDataJpaRepository extends JpaRepository<Team, Long> {
}
