package io.limkhashing.seekmax.presentation.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.limkhashing.seekmax.presentation.ViewState
import io.limkhashing.seekmax.helper.Logger
import io.limkhashing.seekmax.presentation.ui.theme.ButtonColor
import io.limkhashing.seekmax.presentation.ui.theme.SeekMAXAndroidTheme
import io.limkhashing.seekmax.presentation.ui.theme.TextReversedColor
import io.limkhashing.seekmax.presentation.ui.widget.CustomLoadingDialog
import io.limkhashing.seekmax.presentation.ui.widget.LoginField
import io.limkhashing.seekmax.presentation.ui.widget.PasswordField

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onUserLogin: (String, String) -> Unit,
    state: ViewState<String>
) {
    var credentials by remember { mutableStateOf(Credentials()) }
    val context = LocalContext.current.applicationContext

    state.DisplayResult(
        onLoading = {
            CustomLoadingDialog(onDismissRequest = { })
        },
        onSuccess = {
            onLoginSuccess.invoke()
        },
        onError = {
            LaunchedEffect(Unit) {
                Logger.logException(state.getRequestStateException())
                Toast.makeText(context, state.getErrorMessageOrNull() ?: "", Toast.LENGTH_SHORT).show()
            }
        }
    )
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(horizontal = 30.dp)
    ) {
        LoginField(
            value = credentials.username,
            onChange = { data -> credentials = credentials.copy(username = data) },
            modifier = Modifier.fillMaxWidth()
        )
        PasswordField(
            value = credentials.password,
            onChange = { data -> credentials = credentials.copy(password = data) },
            submit = {
                if (!isCredentialsValid(credentials, context)) {
                    credentials = Credentials()
                    return@PasswordField
                }
                onUserLogin(credentials.username, credentials.password)
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                if (!isCredentialsValid(credentials, context)) {
                    credentials = Credentials()
                    return@Button
                }
                onUserLogin(credentials.username, credentials.password)
            },
            enabled = !credentials.isEmpty(),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(ButtonColor)
                .alpha(if (credentials.isEmpty()) 0.5f else 1f)
        ) {
            Text("Login", color = TextReversedColor)
        }
    }
}

fun isCredentialsValid(
    credentials: Credentials,
    context: Context,
): Boolean {
    if (credentials.isEmpty()) {
        Toast.makeText(context, "Wrong Credentials", Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}

data class Credentials(
    var username: String = "",
    var password: String = "",
) {
    fun isEmpty(): Boolean {
        return username.isEmpty() || password.isEmpty()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    SeekMAXAndroidTheme {
        LoginScreen(onLoginSuccess = {}, onUserLogin = { _, _ -> }, state = ViewState.Idle)
    }
}