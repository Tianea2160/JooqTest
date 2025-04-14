package org.example.jooqtest.domain.user.entity

import java.time.LocalDateTime


class User(
    val userId: Int,
    val username: String,
    val teamId: Int,
    val createdAt: LocalDateTime,
)