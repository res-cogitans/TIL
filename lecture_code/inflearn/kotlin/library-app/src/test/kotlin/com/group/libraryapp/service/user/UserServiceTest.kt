package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
open class UserServiceTest @Autowired constructor(
        private val userRepository: UserRepository,
        private val userService: UserService
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
        userRepository.saveAll(listOf(
                User("A", 10),
                User("B", 15),
                User("C", null)
        ))

        val users = userService.getUsers()

        assertThat(users).hasSize(3)
        assertThat(users).extracting("name").containsExactlyInAnyOrder("A", "B", "C")
        assertThat(users).extracting("age").containsExactlyInAnyOrder(10, 15, null)
    }
}