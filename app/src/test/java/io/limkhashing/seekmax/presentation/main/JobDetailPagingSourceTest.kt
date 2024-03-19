package io.limkhashing.seekmax.presentation.main

import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import io.limkhashing.seekmax.domain.model.JobDetail
import io.limkhashing.seekmax.domain.model.PublishedJob
import io.limkhashing.seekmax.domain.repository.job.JobsRepository
import io.limkhashing.seekmax.data.paging.JobDetailPagingSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class JobDetailPagingSourceTest {

    private val jobsRepository: JobsRepository = mockk()
    private lateinit var pagingSource: JobDetailPagingSource
    private lateinit var publishedJob: PublishedJob

    @Before
    fun setUp() {
        val jobDetail = JobDetail(
            "id",
            "title",
            false,
            "positionTitle"
        )
        val jobs = mutableListOf<JobDetail>()
        for (i in 1..10) {
            jobs.add(jobDetail)
        }
        publishedJob = PublishedJob(jobs, true, 1)
        pagingSource = JobDetailPagingSource(jobsRepository)
    }

    @Test
    fun `when load is called, then it should return LoadResult of ActiveJobs`() = runTest {
        // Given
        val page = publishedJob.page
        coEvery { jobsRepository.fetchPublishedJob(page = page) } returns publishedJob

        // When
        val result = pagingSource.load(
            LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        // Then
        coVerify { jobsRepository.fetchPublishedJob(page = 1) }
        assert(result is LoadResult.Page)
        val loadResult = result as LoadResult.Page<Int, JobDetail>
        assert(loadResult.data.isNotEmpty())
        assert(loadResult.prevKey == null)
        assert(loadResult.nextKey == page + 1)
    }

    @Test
    fun `when load is called with key, then it should return LoadResult of ActiveJobs`() = runTest {
        // Given
        val page = publishedJob.page
        coEvery { jobsRepository.fetchPublishedJob(page = page) } returns publishedJob

        // When
        val result = pagingSource.load(
            LoadParams.Refresh(
                key = page,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        // Then
        coVerify { jobsRepository.fetchPublishedJob(page = page) }
        assert(result is LoadResult.Page)
        val loadResult = result as LoadResult.Page<Int, JobDetail>
        assert(loadResult.data.isNotEmpty())
        assert(loadResult.prevKey == null)
        assert(loadResult.nextKey == page + 1)
    }

    @Test
    fun `when load is called with error, then it should return LoadResult of Error`() = runTest {
        // Given
        val exception = Exception()
        coEvery { jobsRepository.fetchPublishedJob(page = 1) } throws exception

        // When
        val result = pagingSource.load(
            LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        // Then
        coVerify { jobsRepository.fetchPublishedJob(page = 1) }
        assert(result is LoadResult.Error)
        val errorResult = result as LoadResult.Error<Int, JobDetail>
        assert(errorResult.throwable == exception)
    }
}