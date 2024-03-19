package io.limkhashing.seekmax.presentation.main.profile

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.limkhashing.seekmax.presentation.ui.theme.ButtonColor
import io.limkhashing.seekmax.presentation.ui.theme.TextReversedColor

@Composable
fun ProfileScreen(onLogoutClicked: () -> Unit, onEditProfileClicked: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(horizontal = 30.dp)
    ) {

        Button(
            onClick = {
                onEditProfileClicked.invoke()
            },
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(ButtonColor)
        ) {
            Text("Edit Profile", color = TextReversedColor)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                onLogoutClicked.invoke()
            },
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.fillMaxWidth().background(ButtonColor)
        ) {
            Text("Logout", color = TextReversedColor)
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ProfileScreenPreview() {
    ProfileScreen(onLogoutClicked = {}, onEditProfileClicked = {})
}