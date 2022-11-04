package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@SpringBootTest
@Transactional
open class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
    @PersistenceContext private val em: EntityManager,
) {

    @Test
    @DisplayName("유저가 정상적으로 저장되어야 한다.")
    internal fun saveUserTest() {
        val request = UserCreateRequest("테스터", null)

        userService.saveUser(request)

        val users = userRepository.findAll()
        assertThat(users).hasSize(1)
        assertThat(users[0].name).isEqualTo("테스터")
    }

    @Test
    internal fun getUserTest() {
        userRepository.saveAll(listOf(User("A", 10), User("B", 15), User("C", null)))

        val users = userService.getUsers()

        assertThat(users).hasSize(3)
        assertThat(users).extracting("name").containsExactlyInAnyOrder("A", "B", "C")
        assertThat(users).extracting("age").containsExactlyInAnyOrder(10, 15, null)
    }

    @Test
    @DisplayName("대출 기록이 없는 유저도 응답에 포함된다.")
    internal fun getUserLoanHistoriesTest() {
        userRepository.save(User("A", null))

        val results = userService.getUserLoanHistories()

        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).isEmpty()
    }

    @Test
    @DisplayName("여러 건 대출이 정상적으로 조회된다.")
    internal fun getMultipleUserLoanHistories() {
        val savedUser = userRepository.save(User("A", null))
        userLoanHistoryRepository.saveAll(listOf(UserLoanHistory.fixture(savedUser, "책1", UserLoanStatus.LOANED), UserLoanHistory.fixture(savedUser, "책2", UserLoanStatus.LOANED), UserLoanHistory.fixture(savedUser, "책3", UserLoanStatus.RETURNED)))

        em.flush()
        em.clear()

        val results = userService.getUserLoanHistories()

        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).hasSize(3)
        assertThat(results[0].books).extracting("name").containsExactlyInAnyOrder("책1", "책2", "책3")
        assertThat(results[0].books).extracting("isReturn").containsExactlyInAnyOrder(false, false, true)

    }

    @Disabled   //굳이 테스트를 합치는 것은 유지보수성도 나쁘며, 테스트 품질도 떨어진다.
    @Test
    @DisplayName("위의 두 테스트를 한 번에 수행한다.")
    internal fun getSingleAndMultipleUserLoanHistories() {
        val savedUsers = userRepository.saveAll(listOf(
            User("A", null),
            User("B", null),
        ))
        userLoanHistoryRepository.saveAll(listOf(
            UserLoanHistory.fixture(savedUsers[0], "책1", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(savedUsers[0], "책2", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(savedUsers[0], "책3", UserLoanStatus.RETURNED)))

        em.flush()
        em.clear()

        val results = userService.getUserLoanHistories()

        assertThat(results).hasSize(2)
        val userAResult = results.first { it.name == "A" }

        assertThat(userAResult.books).hasSize(3)
        assertThat(userAResult.books).extracting("name")
            .containsExactlyInAnyOrder("책1", "책2", "책3")
        assertThat(userAResult.books).extracting("isReturn")
            .containsExactlyInAnyOrder(false, false, true)

        val userBResult = results.first { it.name == "B" }
        assertThat(userBResult.books).isEmpty()
    }
}