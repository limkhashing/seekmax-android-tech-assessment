package io.limkhashing.seekmax.presentation.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.limkhashing.seekmax.data.paging.JobDetailPagingSource
import io.limkhashing.seekmax.domain.repository.job.JobsRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val jobsRepository: JobsRepository
): ViewModel() {

    val publishedJobs = Pager(PagingConfig(pageSize = 5)) {
        JobDetailPagingSource(jobsRepository)
    }.flow.cachedIn(viewModelScope)
}