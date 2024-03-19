package io.limkhashing.seekmax.helper.extensions

fun List<com.apollographql.apollo3.api.Error>?.handleNetworkError(): Exception? {
    return this?.firstOrNull()?.let { Exception(it.message) }
}