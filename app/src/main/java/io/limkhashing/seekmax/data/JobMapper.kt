package io.limkhashing.seekmax.data

import io.limkhashing.ActiveJobsQuery
import io.limkhashing.JobQuery
import io.limkhashing.seekmax.domain.model.JobDetail
import io.limkhashing.seekmax.domain.model.PublishedJob

fun ActiveJobsQuery.Active?.toPublishedJob(): PublishedJob? {
    if (this?.jobs == null) return null
    return PublishedJob(
        jobs = jobs.map { it?.toSimpleJob() ?: return null},
        hasNext = hasNext ?: false,
        page = page ?: 0
    )
}

fun ActiveJobsQuery.Job.toSimpleJob(): JobDetail {
    return JobDetail(
        id = _id,
        description = description,
        haveIApplied = haveIApplied ?: false,
        positionTitle = positionTitle,
    )
}

fun JobQuery.Job?.toJobDetail(): JobDetail? {
    if (this == null) return null
    return JobDetail(
        id = _id,
        description = description,
        haveIApplied = haveIApplied,
        positionTitle = positionTitle,
    )
}