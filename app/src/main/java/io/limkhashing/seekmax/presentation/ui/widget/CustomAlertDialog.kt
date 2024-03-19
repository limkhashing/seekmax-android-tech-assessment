package io.limkhashing.seekmax.presentation.ui.widget

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomAlertDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        modifier = Modifier.padding(6.dp),
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "Change password success!")
        },
        text = {
            Text("You have successfully changed your password.")
        },
        confirmButton = {
            Button(onClick = {
                onDismissRequest.invoke()
            }) {
                Text("Got it")
            }
        }
    )
}