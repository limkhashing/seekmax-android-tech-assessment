package io.limkhashing.seekmax.data.repository

import com.apollographql.apollo3.ApolloClient
import io.limkhashing.ActiveJobsQuery
import io.limkhashing.ApplyJobMutation
import io.limkhashing.JobQuery
import io.limkhashing.seekmax.data.toPublishedJob
import io.limkhashing.seekmax.data.toJobDetail
import io.limkhashing.seekmax.domain.model.JobDetail
import io.limkhashing.seekmax.domain.model.PublishedJob
import io.limkhashing.seekmax.presentation.ViewState
import io.limkhashing.seekmax.helper.extensions.handleNetworkError
import io.limkhashing.seekmax.domain.repository.job.JobsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class JobsRepositoryImpl(private val apolloClient: ApolloClient) : JobsRepository {

    override suspend fun fetchPublishedJob(limit: Int, page: Int): PublishedJob {
        val response = apolloClient.query(ActiveJobsQuery(limit, page)).execute()
        return response.data?.active?.toPublishedJob() ?: throw Exception("No published jobs found")
    }

    override suspend fun fetchJobDetail(id: String): Flow<ViewState<JobDetail>> = flow {
        emit(ViewState.Loading)
        val response = apolloClient.query(JobQuery(id)).execute()
        response.errors.handleNetworkError()?.let { exception ->
            return@flow emit(ViewState.Error(exception))
        }
        val jobDetail = response.data?.job?.toJobDetail() ?: return@flow emit(ViewState.Error(Exception("No job found")))
        emit(ViewState.Success(jobDetail))
    }

    override suspend fun applyJob(id: String): Flow<ViewState<Boolean>> = flow {
        emit(ViewState.Loading)
        val response = apolloClient.mutation(ApplyJobMutation(id)).execute()
        response.errors.handleNetworkError()?.let { exception ->
            return@flow emit(ViewState.Error(exception))
        }
        val success = response.data?.apply ?: return@flow emit(ViewState.Error(Exception("Failed to apply job")))
        emit(ViewState.Success(success))
    }
}