package io.limkhashing.seekmax.data.repository

import com.apollographql.apollo3.ApolloClient
import io.limkhashing.ChangePasswordMutation
import io.limkhashing.seekmax.presentation.ViewState
import io.limkhashing.seekmax.domain.repository.profile.ProfileRepository
import io.limkhashing.seekmax.helper.extensions.handleNetworkError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileRepositoryImpl(
    private val apolloClient: ApolloClient,
) : ProfileRepository {

    // TODO add more function to change user's profile attributes. E.g. name, email, etc.

    override suspend fun changePassword(password: String): Flow<ViewState<Boolean>> = flow {
        emit(ViewState.Loading)
        val response = apolloClient.mutation(ChangePasswordMutation(password)).execute()
        response.errors.handleNetworkError()?.let { exception ->
            return@flow emit(ViewState.Error(exception))
        }
        val auth = response.data?.changepassword ?: return@flow emit(ViewState.Error(Exception("Failed to change password")))
        emit(ViewState.Success(auth))
    }
}