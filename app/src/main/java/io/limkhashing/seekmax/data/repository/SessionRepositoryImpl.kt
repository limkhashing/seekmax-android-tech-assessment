package io.limkhashing.seekmax.data.repository

import com.apollographql.apollo3.ApolloClient
import io.limkhashing.AuthenticationMutation
import io.limkhashing.seekmax.core.manager.SessionManager
import io.limkhashing.seekmax.presentation.ViewState
import io.limkhashing.seekmax.helper.extensions.handleNetworkError
import io.limkhashing.seekmax.domain.repository.login.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SessionRepositoryImpl(
    private val apolloClient: ApolloClient,
    private val sessionManager: SessionManager
) : SessionRepository {

    override fun authenticateUser(username: String, password: String): Flow<ViewState<String>> = flow {
        emit(ViewState.Loading)
        val response = apolloClient.mutation(AuthenticationMutation(username, password)).execute()
        response.errors.handleNetworkError()?.let { exception ->
            return@flow emit(ViewState.Error(exception))
        }
        val auth = response.data?.auth ?: return@flow emit(ViewState.Error(Exception("Failed to authenticate user")))
        emit(ViewState.Success(auth))
    }

    override fun getJwtSession(): String? {
        return sessionManager.getJwtSession()
    }

    override suspend fun setJwtSession(userJWT: String) {
        sessionManager.setJwtSession(userJWT)
    }

    override fun clearSession() {
        sessionManager.clearSession()
    }
}