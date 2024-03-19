package io.limkhashing.seekmax.presentation.main.home.jobdetail

import androidx.lifecycle.SavedStateHandle
import io.limkhashing.seekmax.BaseViewModelTest
import io.limkhashing.seekmax.domain.model.JobDetail
import io.limkhashing.seekmax.presentation.ViewState
import io.limkhashing.seekmax.domain.repository.job.JobsRepository
import io.limkhashing.seekmax.navigation.graph.JOB_ID_ARGUMENTS
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class JobDetailViewModelTest: BaseViewModelTest() {

    private val jobsRepository: JobsRepository = mockk(relaxed = true)
    private lateinit var jobDetailViewModel: JobDetailViewModel
    private val savedStateHandle: SavedStateHandle = mockk()

    @Before
    fun setUp() {
        val savedStateHandle = SavedStateHandle().apply { set(JOB_ID_ARGUMENTS, "") }
        jobDetailViewModel = JobDetailViewModel(jobsRepository, savedStateHandle)
    }

    @Test
    fun `fetchJobDetail should set state to success when job detail is fetched successfully`() = runBlocking {
        // Given
        val jobId = "jobId"
        val jobDetail = JobDetail(
            "id",
            "title",
            false,
            "positionTitle"
        )
        coEvery { savedStateHandle.get<String>(JOB_ID_ARGUMENTS) } returns jobId
        coEvery { jobsRepository.fetchJobDetail(jobId) } returns flowOf(ViewState.Success(jobDetail))

        // When
        jobDetailViewModel.fetchJobDetail(jobId)

        // Then
        coVerify(exactly = 1) { jobsRepository.fetchJobDetail(jobId) }
        assertEquals(ViewState.Success(jobDetail), jobDetailViewModel.jobDetailState.value)
    }

    @Test
    fun `fetchJobDetail should set state to error when job detail is not fetched successfully`() = runBlocking {
        // Given
        val jobId = "jobId"
        val errorException = Exception("some error")
        coEvery { savedStateHandle.get<String>(JOB_ID_ARGUMENTS) } returns jobId
        coEvery { jobsRepository.fetchJobDetail(jobId) } returns flowOf(ViewState.Error(errorException))

        // When
        jobDetailViewModel.fetchJobDetail(jobId)

        // Then
        coVerify(exactly = 1) { jobsRepository.fetchJobDetail(jobId) }
        assertEquals(ViewState.Error(errorException), jobDetailViewModel.jobDetailState.value)
    }

    @Test
    fun `applyJob should set state success when job is applied successfully`() = runBlocking {
        // Given
        val jobId = "jobId"
        coEvery { savedStateHandle.get<String>(JOB_ID_ARGUMENTS) } returns jobId
        coEvery { jobsRepository.applyJob(jobId) } returns flowOf(ViewState.Success(true))

        // When
        jobDetailViewModel.applyJob(jobId)

        // Then
        coVerify(exactly = 1) { jobsRepository.applyJob(jobId) }
        assertEquals(ViewState.Success(true), jobDetailViewModel.applyJobState.value)
    }

    @Test
    fun `applyJob should set state to error when job is not applied successfully`() = runBlocking {
        // Given
        val jobId = "jobId"
        val errorException = Exception("some error")
        coEvery { savedStateHandle.get<String>(JOB_ID_ARGUMENTS) } returns jobId
        coEvery { jobsRepository.applyJob(jobId) } returns flowOf(ViewState.Error(errorException))

        // When
        jobDetailViewModel.applyJob(jobId)

        // Then
        coVerify(exactly = 1) { jobsRepository.applyJob(jobId) }
        assertEquals(ViewState.Error(errorException), jobDetailViewModel.applyJobState.value)
    }
}