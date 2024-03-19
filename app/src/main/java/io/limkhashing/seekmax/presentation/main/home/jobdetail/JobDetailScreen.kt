package io.limkhashing.seekmax.presentation.main.home.jobdetail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.limkhashing.seekmax.presentation.ViewState
import io.limkhashing.seekmax.domain.model.JobDetail
import io.limkhashing.seekmax.helper.Logger
import io.limkhashing.seekmax.presentation.ui.theme.ButtonColor
import io.limkhashing.seekmax.presentation.ui.theme.CardBackgroundColor
import io.limkhashing.seekmax.presentation.ui.theme.GreenColor
import io.limkhashing.seekmax.presentation.ui.theme.TextPrimaryColor
import io.limkhashing.seekmax.presentation.ui.theme.TextReversedColor
import io.limkhashing.seekmax.presentation.ui.theme.TextSecondaryColor
import io.limkhashing.seekmax.presentation.ui.widget.CustomLoadingDialog

@Composable
fun JobDetailScreen(
    jobDetailState: ViewState<JobDetail>,
    applyJobState: ViewState<Boolean>,
    onJobApplied: (isJobApplied: Boolean?) -> Unit
) {
    val context = LocalContext.current.applicationContext

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        applyJobState.DisplayResult(
            onLoading = {
                CustomLoadingDialog(onDismissRequest = {})
            },
            onSuccess = {
                LaunchedEffect(Unit) {
                    Toast.makeText(context, "Applied for job!", Toast.LENGTH_SHORT).show()
                    onJobApplied(applyJobState.getSuccessData())
                }
            },
            onError = {
                LaunchedEffect(Unit) {
                    val exception = applyJobState.getRequestStateException()
                    Logger.logException(exception)
                    Toast.makeText(context, exception?.message ?: "", Toast.LENGTH_SHORT).show()
                }
            }
        )
        jobDetailState.DisplayResult(
            onLoading = {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp)
                )
            },
            onSuccess = {
                val jobDetail = jobDetailState.getSuccessData()
                JobDetailCard(jobDetail = jobDetail)
            },
            onError = {
                LaunchedEffect(Unit) {
                    val exception = jobDetailState.getRequestStateException()
                    Logger.logException(exception)
                    Toast.makeText(context, exception?.message ?: "", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}

@Composable
fun JobDetailCard(jobDetail: JobDetail?) {
    if (jobDetail == null) {
        return
    }
    val viewModel = hiltViewModel<JobDetailViewModel>()
    val hasApplied = jobDetail.haveIApplied ?: false
    Card(
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = jobDetail.positionTitle ?: "No position title",
                    fontSize = 24.sp,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    color = TextPrimaryColor,
                )
                if (jobDetail.haveIApplied == true) {
                    Text(
                        text = "Applied",
                        fontSize = 12.sp,
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        color = GreenColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Company name ${jobDetail.id}",
                fontSize = 12.sp,
                color = TextSecondaryColor,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = jobDetail.description ?: "No description",
                color = TextPrimaryColor,
                fontSize = 16.sp,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                viewModel.applyJob(jobDetail.id ?: return@Button)
            },
            enabled = jobDetail.haveIApplied?.not() ?: false,
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(if (hasApplied) ButtonColor.copy(alpha = 0.5f) else ButtonColor)
        ) {
            val isApplied = if (hasApplied) "You have applied for this job!" else "Apply"
            Text(isApplied, color = TextReversedColor)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun JobDetailScreenPreview() {
    JobDetailScreen(
        jobDetailState = ViewState.Success(
            JobDetail(
                id = "1",
                positionTitle = "Software Engineer",
                description = "This is a software engineer job",
                haveIApplied = false
            )
        ),
        applyJobState = ViewState.Idle,
        onJobApplied = {}
    )
}