package io.limkhashing.seekmax.presentation.main.home.jobdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.limkhashing.seekmax.domain.model.JobDetail
import io.limkhashing.seekmax.presentation.ViewState
import io.limkhashing.seekmax.domain.repository.job.JobsRepository
import io.limkhashing.seekmax.navigation.graph.JOB_ID_ARGUMENTS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobDetailViewModel @Inject constructor(
    private val jobsRepository: JobsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val jobId = savedStateHandle.get<String>(JOB_ID_ARGUMENTS) ?: ""

    private val _jobDetailState = MutableStateFlow<ViewState<JobDetail>>(ViewState.Idle)
    val jobDetailState = _jobDetailState.asStateFlow()

    private val _applyJobState = MutableStateFlow<ViewState<Boolean>>(ViewState.Idle)
    val applyJobState = _applyJobState.asStateFlow()

    init {
        fetchJobDetail(jobId)
    }

    fun fetchJobDetail(jobId: String) = viewModelScope.launch {
        val response = jobsRepository.fetchJobDetail(jobId)
        response.collect {
            _jobDetailState.value = it
        }
    }

    fun applyJob(jobId: String) = viewModelScope.launch {
        val response = jobsRepository.applyJob(jobId)
        response.collect {
            _applyJobState.value = it
        }
    }
}