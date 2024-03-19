package io.limkhashing.seekmax.presentation.main.profile.editprofile

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
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.limkhashing.seekmax.presentation.ViewState
import io.limkhashing.seekmax.helper.Logger
import io.limkhashing.seekmax.presentation.ui.theme.ButtonColor
import io.limkhashing.seekmax.presentation.ui.theme.TextReversedColor
import io.limkhashing.seekmax.presentation.ui.widget.CustomAlertDialog
import io.limkhashing.seekmax.presentation.ui.widget.CustomLoadingDialog
import io.limkhashing.seekmax.presentation.ui.widget.PasswordField

@Composable
fun EditProfileScreen(
    state: ViewState<Boolean>,
    onChangePasswordClicked: (password: String) -> Unit,
    onChangePasswordSuccess: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current.applicationContext

    state.DisplayResult(
        onLoading = {
            CustomLoadingDialog(onDismissRequest = {})
        },
        onSuccess = {
            CustomAlertDialog(onDismissRequest = {
                onChangePasswordSuccess.invoke()
            })
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
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
    ) {
        PasswordField(
            value = password,
            onChange = { data -> password = data },
            submit = {
                onChangePasswordClicked(password)
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                onChangePasswordClicked(password)
            },
            enabled = password.isNotBlank(),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(ButtonColor)
        ) {
            Text("Change Password", color = TextReversedColor)
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ProfileScreenPreview() {
    EditProfileScreen(
        state = ViewState.Idle,
        onChangePasswordClicked = {},
        onChangePasswordSuccess = {}
    )
}