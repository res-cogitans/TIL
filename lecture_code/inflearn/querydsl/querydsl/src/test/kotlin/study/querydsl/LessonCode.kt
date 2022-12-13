package study.querydsl

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import study.querydsl.domain.Member
import study.querydsl.domain.QMember.member
import study.querydsl.domain.QTeam.team
import study.querydsl.domain.Team

@Transactional
@SpringBootTest
class LessonCode @Autowired constructor(
    val emf: EntityManagerFactory,
    val em: EntityManager,
    val queryFactory: JPAQueryFactory
) {

    @Test
    internal fun qMember() {
//        val qMember = QMember("m")
//        val qMember = QMember.member

        val foundMember = queryFactory
            .select(member)
            .from(member)
            .where(member.username.eq("member1"))
            .fetchOne()
    }

    @Test
    internal fun searchQuery() {
        val result = queryFactory
            .select(member)
            .from(member)
            .where(
                member.username.eq("member1")
                    .and(member.age.eq(10))
            )
            .fetchOne()
    }

    @Test
    internal fun theta_join() {
        em.persist(Team("teamA"))
        em.persist(Team("teamB"))
        em.persist(Team("teamC"))
        em.persist(Member(username = "teamA", team = null))
        em.persist(Member(username = "teamB", team = null))

        val result = queryFactory
            .select(member)
            .from(member, team)
            .where(member.username.eq(team.name))
            .fetch()

        assertThat(result)
            .extracting("username")
            .containsExactly("teamA", "teamB")
    }

    /**
     * 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     * JPQL: SELECT m, t FROM Member m LEFT JOIN m.team t on t.name = 'teamA'
     */
    @Test
    internal fun join_on_filtering() {
        val result = queryFactory
            .select(member, team)
            .from(member)
            .leftJoin(member.team, team).on(team.name.eq("teamA"))
            .fetch()
    }

    /**
     * 연관관계 없는 엔티티 외부 조인
     * 회원의 이름이 팀 이름과 같은 대상 외부 조인
     */
    @Test
    internal fun join_on_no_relation() {
        em.persist(Team("teamA"))
        em.persist(Team("teamB"))
        em.persist(Team("teamC"))
        em.persist(Member(username = "teamA", team = null))
        em.persist(Member(username = "teamB", team = null))

        val result = queryFactory
            .select(member, team)
            .from(member)
            .leftJoin(team).on(member.username.eq(team.name))
            .fetch()
    }

    @Test
    internal fun fetch_join_not_applied() {
        val team1 = Team("teamA")
        em.persist(team1)
        val member1 = Member(username = "memberA", team = team1)
        em.persist(member1)

        em.flush()
        em.clear()
        val proxy = em.getReference(Team::class.java, team1.id)
        println("proxy.javaClass = ${proxy.javaClass}")

        val foundMember = queryFactory
            .select(member)
            .from(member)
            .where(member.username.eq("memberA"))
            .fetchOne()

        println(foundMember!!.team!!.javaClass)

        val isProxyEntityLoaded = emf.persistenceUnitUtil.isLoaded(foundMember!!.team)
        assertThat(isProxyEntityLoaded).isFalse
    }
}