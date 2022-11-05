package com.group.libraryapp.controller

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager

@Configuration
class QueryDslConfig(
    private val em: EntityManager,
) {

    @Bean
    fun queryDsl(): JPAQueryFactory {
        return JPAQueryFactory(em)
    }
}