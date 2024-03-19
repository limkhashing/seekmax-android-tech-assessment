import io.limkhashing.seekmax.BaseViewModelTest
import io.limkhashing.seekmax.presentation.ViewState
import io.limkhashing.seekmax.domain.repository.profile.ProfileRepository
import io.limkhashing.seekmax.presentation.main.profile.editprofile.EditProfileViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.lang.Exception

class EditProfileViewModelTest: BaseViewModelTest() {

    private val profileRepository: ProfileRepository = mockk()
    private lateinit var viewModel: EditProfileViewModel

    @Before
    fun setUp() {
        viewModel = EditProfileViewModel(profileRepository)
    }

    @Test
    fun `when changePassword is called with empty password, then it should not call profileRepository changePassword`() = runTest {
        // Given
        val password = ""

        // When
        viewModel.changePassword(password)

        // Then
        coVerify(exactly = 0) { profileRepository.changePassword(any()) }
    }

    @Test
    fun `when changePassword is called with non-empty password, then it should call profileRepository changePassword`() = runTest {
        // Given
        val password = "password"
        coEvery { profileRepository.changePassword(password) } returns flowOf(ViewState.Success(true))

        // When
        viewModel.changePassword(password)

        // Then
        coVerify { profileRepository.changePassword(password) }
    }

    @Test
    fun `when changePassword is called with non-empty password and returns loading, then it should update state to Loading`() = runTest {
        // Given
        val password = "password"
        coEvery { profileRepository.changePassword(password) } returns flowOf(ViewState.Loading)

        // When
        viewModel.changePassword(password)

        // Then
        assert(viewModel.state.value is ViewState.Loading)
    }

    @Test
    fun `when changePassword is called with non-empty password and returns success, then it should update state to Success`() = runTest {
        // Given
        val password = "password"
        coEvery { profileRepository.changePassword(password) } returns flowOf(ViewState.Success(true))

        // When
        viewModel.changePassword(password)

        // Then
        assert(viewModel.state.value is ViewState.Success)
    }

    @Test
    fun `when changePassword is called with non-empty password and returns error, then it should update state to Error`() = runTest {
        // Given
        val password = "password"
        coEvery { profileRepository.changePassword(password) } returns flowOf(ViewState.Error(Exception("error")))

        // When
        viewModel.changePassword(password)

        // Then
        assert(viewModel.state.value is ViewState.Error)
    }
}