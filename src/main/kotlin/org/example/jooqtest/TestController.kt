package org.example.jooqtest

import org.example.jooqtest.domain.user.dto.UserDetailResponse
import org.example.jooqtest.domain.user.entity.User
import org.example.jooqtest.domain.user.repository.UserJoopRepositoryImpl
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
    private val repository: UserJoopRepositoryImpl,
) {

    @GetMapping("/user/{userId}")
    fun findById(@PathVariable userId: Int): User? = repository.findByUserId(userId)

    @GetMapping("/user/detail/{userId}")
    fun findByUserId(@PathVariable userId: Int): UserDetailResponse? = repository.findDetailsByUserId(userId)
}