package study.querydsl.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class TestEntityForQClass(
    @Id @GeneratedValue val id: Long,
    val name: String
) {
}