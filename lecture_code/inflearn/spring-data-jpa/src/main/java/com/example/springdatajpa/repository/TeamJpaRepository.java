package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Team;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Team save(Team team) {
        em.persist(team);
        return team;
    }

    public Optional<Team> findById(Long id) {
        return Optional.ofNullable(em.find(Team.class, id));
    }

    public List<Team> findAll() { //JPQL 사용
        return em.createQuery("SELECT t FROM Team t", Team.class)
                .getResultList();
    }

    public void delete(Team team) {
        em.remove(team);
    }

    public long count() {
        return em.createQuery("SELECT COUNT(t) FROM Team t", Long.class)
                .getSingleResult();
    }
}
