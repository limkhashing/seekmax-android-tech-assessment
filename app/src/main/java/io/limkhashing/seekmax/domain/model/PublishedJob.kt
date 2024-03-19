package io.limkhashing.seekmax.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class PublishedJob(
    val jobs: List<JobDetail>?,
    val hasNext: Boolean,
    val page: Int
)

@Immutable
data class JobDetail(
    val id: String?,
    val description: String?,
    val haveIApplied: Boolean?,
    val positionTitle: String?,
)