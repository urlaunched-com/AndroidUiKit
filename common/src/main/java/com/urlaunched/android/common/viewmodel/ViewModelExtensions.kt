package com.urlaunched.android.common.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.urlaunched.android.common.response.ErrorData
import com.urlaunched.android.common.response.Response

suspend fun <T : Any> ViewModel.performUseCase(
    useCase: suspend () -> Response<T>,
    success: suspend (data: T) -> Unit,
    error: suspend (error: ErrorData) -> Unit
) {
    val useCaseParams = getUseCaseParams(useCase)
    when (val response = useCase()) {
        is Response.Success -> {
            logFireBaseMessage(params = useCaseParams, responseData = response.data.toString(), isSuccess = true)
            success(response.data)
        }

        is Response.Error -> {
            logFireBaseMessage(params = useCaseParams, responseData = response.error.toString(), isSuccess = false)
            error(response.error)
        }
    }
}

inline fun ViewModel.loadingTask(setLoading: (isLoading: Boolean) -> Unit, block: () -> Unit) {
    try {
        setLoading(true)
        block()
    } finally {
        setLoading(false)
    }
}

private fun <T : Any> getUseCaseParams(useCase: T): String {
    val useCaseClass = useCase::class.java
    return useCaseClass.declaredFields
        .filterNot { it.name in setOf("label", "this$0") }
        .joinToString { field ->
            field.isAccessible = true
            val value = field.get(useCase)
            "${field.name.removePrefix("$")} = $value"
        }
}

private fun logFireBaseMessage(params: String, responseData: String, isSuccess: Boolean) {
    val data = if (isSuccess) {
        SUCCESS_MESSAGE.format(responseData)
    } else {
        ERROR_MESSAGE.format(responseData)
    }

    Firebase.crashlytics.log(FIREBASE_MESSAGE.format(params, data))
}

const val FIREBASE_MESSAGE = "performUseCase(%s) %s"
const val SUCCESS_MESSAGE = "Success: %s"
const val ERROR_MESSAGE = "Error %s"