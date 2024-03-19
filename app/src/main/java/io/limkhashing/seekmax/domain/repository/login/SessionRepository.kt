package io.limkhashing.seekmax.domain.repository.login

import io.limkhashing.seekmax.presentation.ViewState
import kotlinx.coroutines.flow.Flow


interface SessionRepository {
    fun authenticateUser(username: String, password: String): Flow<ViewState<String>>

    fun getJwtSession(): String?
    suspend fun setJwtSession(userJWT: String)
    fun clearSession()
}