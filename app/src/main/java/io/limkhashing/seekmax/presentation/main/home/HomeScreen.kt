package io.limkhashing.seekmax.presentation.main.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.limkhashing.seekmax.domain.model.JobDetail
import io.limkhashing.seekmax.presentation.ui.theme.CardBackgroundColor
import io.limkhashing.seekmax.presentation.ui.theme.GreenColor
import io.limkhashing.seekmax.presentation.ui.theme.TextPrimaryColor
import io.limkhashing.seekmax.presentation.ui.theme.TextSecondaryColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    isJobApplied: Boolean,
    publishedJobs: LazyPagingItems<JobDetail>?,
    onJobItemCardClicked: (jobId: String) -> Unit
) {
    if (publishedJobs == null) return
    val context = LocalContext.current.applicationContext
    val loadState = publishedJobs.loadState.refresh
    val isLoading = loadState is LoadState.Loading
    val lazyListState = rememberLazyListState()
    val pullRefreshState = rememberPullRefreshState(isLoading, { publishedJobs.refresh() })
    val shouldRefreshList = remember(isJobApplied) { isJobApplied && publishedJobs.itemCount > 0 }

    // TODO add search bar and filter the current jobs list
    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .pullRefresh(pullRefreshState),
    ) {
        LaunchedEffect(key1 = shouldRefreshList) {
            if (shouldRefreshList) {
                launch(Dispatchers.Main) {
                    lazyListState.animateScrollToItem(0)
                    publishedJobs.refresh()
                }
            }
        }
        LaunchedEffect(key1 = loadState) {
            if (loadState is LoadState.Error) {
                Toast.makeText(context, loadState.error.message ?: "", Toast.LENGTH_SHORT).show()
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, bottom = 100.dp),
                contentPadding = PaddingValues(all = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    count = publishedJobs.itemCount,
                ) { index ->
                    val simpleJob = publishedJobs[index] ?: return@items
                    JobDetailItemCard(
                        jobDetail = simpleJob,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .clickable {
                                onJobItemCardClicked(simpleJob.id ?: return@clickable)
                            }
                    )
                }
            }
            PullRefreshIndicator(isLoading, pullRefreshState, Modifier.align(Alignment.TopCenter))
        }
    }
}

@Composable
private fun JobDetailItemCard(
    jobDetail: JobDetail,
    modifier: Modifier
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        modifier = modifier,
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
                text = "Company name: ${jobDetail.id}",
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
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreView() {
    HomeScreen(isJobApplied = false, publishedJobs = null, onJobItemCardClicked = {})
}