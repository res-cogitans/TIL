package com.group.libraryapp.domain.book

import java.lang.IllegalArgumentException
import javax.persistence.*
import javax.persistence.EnumType.*
import javax.persistence.GenerationType.*

@Entity
class Book(
    val name: String,
    @Enumerated(STRING) val type: BookType,
    @Id @GeneratedValue(strategy = IDENTITY) val id: Long? = null,
) {
    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다")
        }
    }

    companion object {
        fun fixture(
            name: String = "책 이름",
            type: BookType = BookType.COMPUTER,
            id: Long? = null,
        ): Book {
            return Book(
                name = name,
                type = type,
                id = id,
            )
        }
    }
}

