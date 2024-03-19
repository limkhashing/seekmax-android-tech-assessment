package io.limkhashing.seekmax.presentation.login

import io.limkhashing.seekmax.BaseViewModelTest
import io.limkhashing.seekmax.presentation.ViewState
import io.limkhashing.seekmax.domain.repository.login.SessionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LoginViewModelTest: BaseViewModelTest() {

    private val sessionRepository: SessionRepository = mockk()
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        viewModel = LoginViewModel(sessionRepository)
    }

    @Test
    fun `login should set state to Loading when onUserLogin is clicked`() = runTest {
        // Given
        val username = "username"
        val password = "password"
        coEvery { sessionRepository.authenticateUser(username, password) } returns flowOf(ViewState.Loading)

        // When
        viewModel.onUserLogin(username, password)

        // Then
        coVerify(exactly = 1) { sessionRepository.authenticateUser(username, password) }
        assertEquals(ViewState.Loading, viewModel.loginState.value)
    }

    @Test
    fun `login should set state to success when authentication is successful`() = runTest {
        // Given
        val username = "username"
        val password = "password"
        val jwt = "jwt"
        coEvery { sessionRepository.authenticateUser(username, password) } returns flowOf(ViewState.Success(jwt))

        // When
        viewModel.onUserLogin(username, password)

        // Then
        coVerify(exactly = 1) { sessionRepository.authenticateUser(username, password) }
        assertEquals(ViewState.Success(jwt), viewModel.loginState.value)
    }

    @Test
    fun `login should set state to error when authentication fails`() = runBlocking {
        // Given
        val username = "username"
        val password = "password"
        val errorException = Exception("some error")
        coEvery { sessionRepository.authenticateUser(username, password) } returns flowOf(ViewState.Error(errorException))

        // When
        viewModel.onUserLogin(username, password)

        // Then
        coVerify(exactly = 1) { sessionRepository.authenticateUser(username, password) }
        assertEquals(ViewState.Error(errorException), viewModel.loginState.value)
    }

    @Test
    fun `setJwtSession should call sessionRepository to set jwt session`() = runBlocking {
        // Given
        val jwt = "jwt"

        // When
        viewModel.setJwtSession(jwt)

        // Then
        coVerify(exactly = 1) { sessionRepository.setJwtSession(jwt) }
    }

    @Test
    fun `getJwtSession should return jwt session from sessionRepository`() = runBlocking {
        // Given
        val jwt = "jwt"
        coEvery { sessionRepository.getJwtSession() } returns jwt

        // When
        val result = viewModel.getJwtSession()

        // Then
        coVerify(exactly = 1) { sessionRepository.getJwtSession() }
        assertEquals(jwt, result)
    }
}