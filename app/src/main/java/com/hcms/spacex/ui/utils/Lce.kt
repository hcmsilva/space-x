package com.hcms.spacex.ui.utils

//Ended up not having time to refactor
data class Lce<T> @JvmOverloads constructor(
    val isLoading: Boolean = false,
    val contentData: T? = null,
    val error: Throwable? = null,
    val hasError: Boolean = error != null,
    val hasContentData: Boolean = contentData != null
) {
    companion object {
        fun <T> content(content: T) = Lce<T>(contentData = content)
        fun <T> loading() = Lce<T>(isLoading = true)
        fun <T> error(t: Throwable) = Lce<T>(error = t)
    }
}