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
    when (val response = useCase()) {
        is Response.Success -> {
            success(response.data)
        }

        is Response.Error -> {
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

private fun <T : Any> getUseCaseParams(useCase: suspend () -> Response<T>): String {
    val useCaseClass = useCase::class.java
    return useCaseClass.declaredFields
        .filterNot { it.name in setOf("label", "this$0") }
        .joinToString { field ->
            field.isAccessible = true
            val value = field.get(useCase)
            "${field.name.removePrefix("$")} = ${value?.toString()}"
        }
}

private fun logFirebaseMessage(params: String, responseData: Any, isSuccess: Boolean, isUseCaseLoggable: Boolean) {
    if (isUseCaseLoggable) {
        val data = if (isSuccess) {
            when {
                responseData is Unit -> SUCCESS_MESSAGE.format(UNIT)
                (responseData as? List<*>)?.isEmpty() == true -> SUCCESS_MESSAGE.format(EMPTY_LIST)
                else -> SUCCESS_MESSAGE.format(responseData.toString())
            }
        } else {
            ERROR_MESSAGE.format(responseData)
        }

        val fireBaseMessage = FIREBASE_MESSAGE.format(params, data)

        Firebase.crashlytics.log(fireBaseMessage)
    }
}

const val FIREBASE_MESSAGE = "performUseCase(%s) %s"
const val SUCCESS_MESSAGE = "Success: %s"
const val ERROR_MESSAGE = "Error: %s"
const val UNIT = "Unit"
const val EMPTY_LIST = "Empty list"