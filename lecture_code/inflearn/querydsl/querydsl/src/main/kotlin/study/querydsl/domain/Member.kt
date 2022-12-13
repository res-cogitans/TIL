package study.querydsl.domain

import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.GenerationType.IDENTITY

@Entity
open class Member(
    @Id @GeneratedValue(strategy = IDENTITY) val id: Long? = null,
    val username: String,
    val age: Int? = null,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    var team: Team?
) {

    fun changeTeam(team: Team) {
        this.team = team
        team.addMember(this)
    }
}