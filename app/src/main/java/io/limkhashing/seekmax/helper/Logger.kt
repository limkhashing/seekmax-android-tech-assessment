package io.limkhashing.seekmax.helper

object Logger {
    fun logException(e: Exception?) {
        // TODO Send exception to crashlytics
        println(e?.message)
    }
}