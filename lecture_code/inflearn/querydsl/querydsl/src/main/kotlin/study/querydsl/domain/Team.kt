package study.querydsl.domain

import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

//@Entity
open class Teams(
//    @Id @GeneratedValue(strategy = IDENTITY) val id: Long? = null,
    val name: String,

//    @OneToMany(mappedBy = "team", fetch = LAZY)
//    val members: MutableList<Member> = mutableListOf()
) {
//    fun addMember(member: Member) {
//        this.members.add(member)
//    }
}
