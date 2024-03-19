package io.limkhashing.seekmax.presentation.main.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.limkhashing.seekmax.domain.repository.login.SessionRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
): ViewModel() {

    fun logout() {
        sessionRepository.clearSession()
    }
}