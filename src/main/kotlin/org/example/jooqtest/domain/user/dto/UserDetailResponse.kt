package org.example.jooqtest.domain.user.dto

import java.time.LocalDateTime

class UserDetailResponse(
    val userId: Int,
    val teamId: Int,
    val username: String,
    val email: String,
    val teamName: String,
    val createdAt: LocalDateTime,
)