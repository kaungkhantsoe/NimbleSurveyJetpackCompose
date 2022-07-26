@file:Suppress("UnusedPrivateMember")

package com.kks.nimblesurveyjetpackcompose.util.extensions

import com.kks.nimblesurveyjetpackcompose.model.ErrorModel
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.response.BaseResponse
import com.kks.nimblesurveyjetpackcompose.model.response.CustomErrorResponse
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

private const val API_WAIT_TIME = 7000L
const val SUCCESS_WITH_NULL_ERROR = "Success with null error"

/**
 * Reference: https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912
 */
@Suppress("TooGenericExceptionCaught")
suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResourceState<T> {
    return withContext(dispatcher) {
        try {
            withTimeout(API_WAIT_TIME) {
                ResourceState.Success(apiCall.invoke())
            }
        } catch (throwable: Exception) {
            when (throwable) {
                is IOException,
                is UnknownHostException,
                is TimeoutCancellationException -> ResourceState.NetworkError
                is HttpException -> {
                    val errorResponse = throwable.response()?.parseJsonErrorResponse<CustomErrorResponse>()
                    val errorMsg = errorResponse?.errors?.first()?.detail ?: "Unknown error"
                    ResourceState.Error(errorMsg)
                }
                else -> ResourceState.Error(throwable.message ?: "Unknown error")
            }
        }
    }
}

// Reference: https://stackoverflow.com/a/55255097
@Suppress("SwallowedException")
inline fun <reified T> Response<*>.parseJsonErrorResponse(): T? {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val parser = moshi.adapter(T::class.java)
    val response = errorBody()?.string()
    return try {
        response?.let { parser.fromJson(it) }
    } catch (e: JsonDataException) {
        null
    }
}

fun <T> ResourceState<T>.mapError(): ErrorModel? {
    return when (this) {
        is ResourceState.Error -> ErrorModel(errorType = ErrorType.INFO, errorMessage = error.orEmpty())
        is ResourceState.NetworkError -> ErrorModel(errorType = ErrorType.NETWORK)
        else -> null
    }
}

enum class ErrorType {
    INFO, NETWORK
}

fun <T> Flow<ResourceState<T>>.catchError() =
    catch { error ->
        emit(ResourceState.Error(error.message.orEmpty()))
    }
