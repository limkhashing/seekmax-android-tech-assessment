package io.limkhashing.seekmax.domain.repository.job

import io.limkhashing.seekmax.domain.model.JobDetail
import io.limkhashing.seekmax.domain.model.PublishedJob
import io.limkhashing.seekmax.presentation.ViewState
import kotlinx.coroutines.flow.Flow

interface JobsRepository {
    // Default limit of pagination is 5, which followed backend
    suspend fun fetchPublishedJob(limit: Int = 5, page: Int): PublishedJob

    suspend fun fetchJobDetail(id: String): Flow<ViewState<JobDetail>>

    suspend fun applyJob(id: String): Flow<ViewState<Boolean>>
}