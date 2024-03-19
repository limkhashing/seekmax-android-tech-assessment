package io.limkhashing.seekmax.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.limkhashing.seekmax.domain.model.JobDetail
import io.limkhashing.seekmax.domain.repository.job.JobsRepository

class JobDetailPagingSource(
    private val jobsRepository: JobsRepository,
) : PagingSource<Int, JobDetail>() {

    override fun getRefreshKey(state: PagingState<Int, JobDetail>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
 
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, JobDetail> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = jobsRepository.fetchPublishedJob(page = nextPageNumber)
            LoadResult.Page(
                data = response.jobs ?: emptyList(),
                prevKey = null,
                nextKey = if (!response.jobs.isNullOrEmpty())
                    response.page + 1
                else
                    null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}