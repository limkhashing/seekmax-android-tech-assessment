package io.limkhashing.seekmax.presentation.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.limkhashing.seekmax.presentation.main.MainActivity
import org.junit.Before

import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun test_login_screen_is_displayed() {
        composeTestRule.onNode(hasText("Username")).assertIsDisplayed()
        composeTestRule.onNode(hasText("Password")).assertIsDisplayed()
        composeTestRule.onNode(hasText("Login")).assertIsDisplayed()
    }

    @Test
    fun test_login_button_should_not_clickable_if_credentials_is_empty() {
        composeTestRule.onNode(hasText("Login")).assertIsDisplayed()
        composeTestRule.onNode(hasText("Login")).assertIsNotEnabled()
    }

    @Test
    fun test_login_button_should_not_clickable_if_username_is_empty() {
        composeTestRule.onNodeWithText("Username").performTextInput("username")
        composeTestRule.onNode(hasText("Login")).assertIsNotEnabled()
    }

    @Test
    fun test_login_button_should_not_clickable_if_password_is_empty() {
        composeTestRule.onNodeWithText("Password").performTextInput("password")
        composeTestRule.onNode(hasText("Login")).assertIsNotEnabled()
    }

    @Test
    fun test_login_button_click_should_clickable_if_credentials_is_not_empty() {
        composeTestRule.onNodeWithText("Username").performTextInput("username")
        composeTestRule.onNodeWithText("Password").performTextInput("password")
        composeTestRule.onNode(hasText("Login")).assertIsEnabled()
    }

    @Test
    fun test_login_button_click_should_not_show_loading_dialog_if_credentials_is_empty() {
        composeTestRule.onNodeWithText("Login").performClick()
        composeTestRule.onNodeWithTag("CustomLoadingDialog").assertDoesNotExist()
    }

    @Test
    fun test_login_button_click_should_show_loading_dialog_if_credentials_is_not_empty() {
        composeTestRule.onNodeWithText("Username").performTextInput("username")
        composeTestRule.onNodeWithText("Password").performTextInput("password")
        composeTestRule.onNodeWithText("Login").performClick()
        composeTestRule.onNodeWithTag("CustomLoadingDialog").assertIsDisplayed()
    }
}