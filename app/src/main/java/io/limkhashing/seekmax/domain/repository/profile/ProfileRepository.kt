package io.limkhashing.seekmax.domain.repository.profile

import io.limkhashing.seekmax.presentation.ViewState
import kotlinx.coroutines.flow.Flow


interface ProfileRepository {
    suspend fun changePassword(password: String): Flow<ViewState<Boolean>>
}