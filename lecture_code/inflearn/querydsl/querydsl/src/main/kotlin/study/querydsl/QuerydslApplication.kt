package study.querydsl

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class QuerydslApplication

fun main(args: Array<String>) {
    runApplication<QuerydslApplication>(*args)
}
