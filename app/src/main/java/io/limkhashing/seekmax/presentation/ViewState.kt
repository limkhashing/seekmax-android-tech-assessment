package io.limkhashing.seekmax.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable

sealed class ViewState<out T> {
    data object Idle : ViewState<Nothing>()
    data object Loading : ViewState<Nothing>()
    data class Success<T>(val data: T) : ViewState<T>()
    data class Error(val exception: java.lang.Exception) : ViewState<Nothing>()

    fun isLoading() = this is Loading
    fun isSuccess() = this is Success
    fun isError() = this is Error

    /**
     * Returns data from a [Success].
     * @throws ClassCastException If the current state is not [Success]
     *  */
    fun getSuccessData() = (this as? Success)?.data
    fun getSuccessDataOrNull(): T? {
        return try {
            (this as Success).data
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Returns an error message from an [Error]
     * @throws ClassCastException If the current state is not [Error]
     *  */
    fun getRequestStateException() = (this as? Error)?.exception
    fun getErrorMessageOrNull(): String? {
        return try {
            (this as Error).exception.message
        } catch (e: Exception) {
            null
        }
    }

    @Composable
    fun DisplayResult(
        onIdle: (@Composable () -> Unit)? = null,
        onLoading: @Composable () -> Unit,
        onSuccess: @Composable () -> Unit,
        onError: @Composable () -> Unit,
    ) {
        AnimatedContent(
            targetState = this,
            transitionSpec = {
                fadeIn(tween(durationMillis = 300)) togetherWith fadeOut(tween(durationMillis = 300))
            },
            label = "Content Animation"
        ) { state ->
            when (state) {
                is Idle -> onIdle?.invoke()
                is Loading -> onLoading()
                is Success -> onSuccess()
                is Error -> onError()
            }
        }
    }
}