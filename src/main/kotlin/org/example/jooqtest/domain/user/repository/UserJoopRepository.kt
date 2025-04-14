package org.example.jooqtest.domain.user.repository

import nu.studer.sample.tables.Team
import nu.studer.sample.tables.Team.*
import nu.studer.sample.tables.Users
import nu.studer.sample.tables.Users.*
import org.example.jooqtest.domain.user.dto.UserDetailResponse
import org.example.jooqtest.domain.user.entity.User
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import kotlin.jvm.java


@Repository
class UserJoopRepositoryImpl(
    private val dslContext: DSLContext,
) {
    fun findByUserId(userId: Int): User? = dslContext
        .selectFrom(USERS)
        .where(USERS.USER_ID.eq(userId))
        .fetchAny { record ->
            User(
                userId = record.userId,
                teamId = record.teamId,
                createdAt = record.createdAt,
                username = record.username,
            )
        }

    fun findDetailsByUserId(userId: Int): UserDetailResponse? {
        return dslContext.select(
            USERS.USER_ID,
            USERS.USERNAME,
            USERS.EMAIL,
            USERS.CREATED_AT,
            TEAM.TEAM_ID,
            TEAM.NAME
        ).from(USERS)
            .join(TEAM).on(USERS.TEAM_ID.eq(TEAM.TEAM_ID))
            .where(USERS.USER_ID.eq(userId))
            .fetchAny { record ->
                UserDetailResponse(
                    userId = record.getValue<Int>(0),
                    username = record.getValue<String>(1),
                    email = record.getValue<String>(2),
                    createdAt = record.getValue<LocalDateTime>(3),
                    teamId = record.getValue<Int>(4),
                    teamName = record.getValue<String>(5),
                )
            }
    }


}

inline infix fun <reified T> Record.getValue(i: Int): T = this.getValue(i, T::class.java)