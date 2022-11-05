package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryQueryDslRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.book.response.BookStatResponse
import com.group.libraryapp.repository.book.BookQueryDslRepository
import com.group.libraryapp.util.fail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class BookService(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
    private val bookQueryDslRepository: BookQueryDslRepository,
    private val userLoanHistoryQueryDslRepository: UserLoanHistoryQueryDslRepository,
) {

    @Transactional
    fun saveBook(request: BookRequest) {
        bookRepository.save(Book.fixture(request.name))
    }

    @Transactional
    fun loanBook(request: BookLoanRequest) {
        val book = bookRepository.findByName(request.bookName) ?: fail()
//        if (userLoanHistoryRepository.findByBookNameAndStatus(request.bookName, UserLoanStatus.LOANED) != null) {
//            throw IllegalArgumentException("진작 대출되어 있는 책입니다")
//        }

        if (userLoanHistoryQueryDslRepository.find(request.bookName, UserLoanStatus.LOANED) != null) {
            throw IllegalArgumentException("진작 대출되어 있는 책입니다")
        }

        val user = userRepository.findByName(request.userName) ?: fail()
        user.loanBook(book)
    }

    @Transactional
    fun returnBook(request: BookReturnRequest) {
        val user = userRepository.findByName(request.userName) ?: fail()
        user.returnBook(request.bookName)
    }

    fun countLoanedBooK(): Int {
        return userLoanHistoryQueryDslRepository.count(UserLoanStatus.LOANED).toInt()
//        return userLoanHistoryRepository.countByStatus(UserLoanStatus.LOANED).toInt()
//        return userLoanHistoryRepository.findAllByStatus(UserLoanStatus.LOANED).size
    }

    fun getBookStatistics(): List<BookStatResponse> {
        return bookQueryDslRepository.getStats()

//        return bookRepository.getStats()

//        return bookRepository.findAll()         //List<Book>
//            .groupBy { book -> book.type }      //Map<BookType, List<Book>>
//            .map { (type, books) -> BookStatResponse(type, books.size) }
    }
}