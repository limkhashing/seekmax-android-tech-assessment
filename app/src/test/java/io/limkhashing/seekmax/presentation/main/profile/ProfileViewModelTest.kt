package io.limkhashing.seekmax.presentation.main.profile

import io.limkhashing.seekmax.BaseViewModelTest
import io.limkhashing.seekmax.domain.repository.login.SessionRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ProfileViewModelTest: BaseViewModelTest() {

    private val sessionRepository: SessionRepository = mockk()
    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        viewModel = ProfileViewModel(sessionRepository)
    }

    @Test
    fun `when logout is called, then it should call sessionRepository clearSession`() = runTest {
        every { sessionRepository.clearSession() } returns Unit
        // When
        viewModel.logout()

        // Then
        coVerify { sessionRepository.clearSession() }
    }
}
