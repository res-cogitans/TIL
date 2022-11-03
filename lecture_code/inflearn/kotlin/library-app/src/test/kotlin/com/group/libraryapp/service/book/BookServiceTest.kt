package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Transactional
@SpringBootTest
open class BookServiceTest @Autowired constructor(
        private val bookRepository: BookRepository,
        private val bookService: BookService,
        private val userRepository: UserRepository,
        private val userLoanHistoryRepository: UserLoanHistoryRepository,
        @PersistenceContext private val em: EntityManager,
) {

    @Test
    @DisplayName("책이 정상적으로 등록된다.")
    internal fun saveBookTest() {
        val request = BookRequest("클린 코드")

        bookService.saveBook(request)

        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books[0].name).isEqualTo("클린 코드")
    }

    @Test
    @DisplayName("책이 정상적으로 대출된다.")
    internal fun loanBookTest() {
        bookRepository.save(Book("이펙티브 자바"))
        userRepository.save(User("테스터", null))
        val request = BookLoanRequest("테스터", "이펙티브 자바")

        bookService.loanBook(request)

        val histories = userLoanHistoryRepository.findAll()
        assertThat(histories).hasSize(1)
        assertThat(histories[0].bookName).isEqualTo("이펙티브 자바")
        assertThat(histories[0].user.name).isEqualTo("테스터")
    }

    @Test
    @DisplayName("책이 이미 대출되어 있는 경우 대출이 실패한다.")
    internal fun loanBookFailTest() {
        val savedBook = bookRepository.save(Book("이펙티브 자바"))
        val savedUser = userRepository.save(User("테스터", null))
        userLoanHistoryRepository.save(UserLoanHistory(savedUser, "이펙티브 자바", false))
        val request = BookLoanRequest("새로운 유저", "이펙티브 자바")

        val message = assertThrows<IllegalArgumentException> {
            bookService.loanBook(request)
        }.message
        assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
    }

    @Test
    @DisplayName("책 반납이 정상적으로 처리된다.")
    internal fun returnBookTest() {
        bookRepository.save(Book("이펙티브 자바"))
        val savedUser = userRepository.save(User("테스터", null))
        val savedHistory = userLoanHistoryRepository.save(UserLoanHistory(savedUser, "이펙티브 자바", false))
        val request = BookReturnRequest("테스터", "이펙티브 자바")
        em.flush()
        em.clear()

        bookService.returnBook(request)

        val histories = userLoanHistoryRepository.findAll()
        assertThat(histories).hasSize(1)
        assertThat(histories[0].isReturn).isTrue
    }
}