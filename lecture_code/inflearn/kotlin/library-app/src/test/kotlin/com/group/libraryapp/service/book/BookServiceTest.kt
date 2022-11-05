package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.book.response.BookStatResponse
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
class BookServiceTest @Autowired constructor(
    private val bookRepository: BookRepository,
    private val bookService: BookService,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
    @PersistenceContext private val em: EntityManager,
) {

    @Test
    @DisplayName("책이 정상적으로 등록된다.")
    internal fun saveBookTest() {
        val request = BookRequest("클린 코드", BookType.COMPUTER)

        bookService.saveBook(request)

        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books[0].name).isEqualTo("클린 코드")
        assertThat(books[0].type).isEqualTo(BookType.COMPUTER)
    }

    @Test
    @DisplayName("책이 정상적으로 대출된다.")
    internal fun loanBookTest() {
        bookRepository.save(Book("이펙티브 자바", BookType.COMPUTER))
        userRepository.save(User("테스터"))
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
        val savedBook = bookRepository.save(Book.fixture("이펙티브 자바"))
        val savedUser = userRepository.save(User("테스터"))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "이펙티브 자바"))
        val request = BookLoanRequest("새로운 유저", "이펙티브 자바")

        val message = assertThrows<IllegalArgumentException> {
            bookService.loanBook(request)
        }.message
        assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
    }

    @Test
    @DisplayName("책 반납이 정상적으로 처리된다.")
    internal fun returnBookTest() {
        bookRepository.save(Book.fixture("이펙티브 자바"))
        val savedUser = userRepository.save(User("테스터"))
        val savedHistory = userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "이펙티브 자바"))
        val request = BookReturnRequest("테스터", "이펙티브 자바")
        em.flush()
        em.clear()

        bookService.returnBook(request)

        val histories = userLoanHistoryRepository.findAll()
        assertThat(histories).hasSize(1)
        assertThat(histories[0].status).isEqualTo(UserLoanStatus.RETURNED)
    }

    @Test
    @DisplayName("책 대여 권수를 정상 확인한다.")
    internal fun countLoanedBookTest() {
        val user = userRepository.save(User("테스터", null))
        userLoanHistoryRepository.saveAll(listOf(
            UserLoanHistory.fixture(user, "A"),
            UserLoanHistory.fixture(user, "B", UserLoanStatus.RETURNED)
        ))

        val result = bookService.countLoanedBooK()

        assertThat(result).isEqualTo(1)
    }

    @Test
    @DisplayName("분야별 책 권수를 정상 확인한다")
    internal fun getBooksStatisticsTest() {
        bookRepository.saveAll(listOf(
            Book.fixture("A"),
            Book.fixture("B", BookType.ECONOMY),
            Book.fixture("C")
        ))

        val results = bookService.getBookStatistics()
        assertCount(results, BookType.COMPUTER, 2L)
        assertCount(results, BookType.ECONOMY, 1L)
    }

    private fun assertCount(results: List<BookStatResponse>, type: BookType, count: Long) {
        assertThat(results.first { result -> result.type == type }.count).isEqualTo(count)
    }

}