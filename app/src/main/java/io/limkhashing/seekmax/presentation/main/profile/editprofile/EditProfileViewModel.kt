package io.limkhashing.seekmax.presentation.main.profile.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.limkhashing.seekmax.presentation.ViewState
import io.limkhashing.seekmax.domain.repository.profile.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
): ViewModel() {

    private val _state = MutableStateFlow<ViewState<Boolean>>(ViewState.Idle)
    val state = _state.asStateFlow()

    fun changePassword(password: String) = viewModelScope.launch {
        if (password.isBlank()) return@launch
        val response = profileRepository.changePassword(password)
        response.collect {
            _state.value = it
        }
    }
}