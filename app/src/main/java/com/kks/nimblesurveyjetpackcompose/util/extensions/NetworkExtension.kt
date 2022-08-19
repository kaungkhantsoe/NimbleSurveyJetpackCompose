package com.kks.nimblesurveyjetpackcompose.util.extensions

import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.response.BaseResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ProtocolException
import java.net.UnknownHostException

private const val API_WAIT_TIME = 7000L
const val UNKNOWN_ERROR = "Unknown error"
const val SUCCESS_WITH_NULL_ERROR = "Success with null error"
const val UNKNOWN_ERROR_MESSAGE = "Unknown error message"
const val NETWORK_ERROR = "Network Error"

const val EMAIL_EMPTY_MESSAGE = "Email cannot be empty"
const val PASSWORD_EMPTY_MESSAGE = "Password cannot be empty"
const val SUCCESS_MESSAGE = "success"

private const val HTTP_ERROR_START = 400
private const val HTTP_ERROR_END = 499
const val INDEX_OF_ERROR_CODE = 0

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
        } catch (throwable: Throwable) {
            when (throwable) {
                is ProtocolException -> ResourceState.ProtocolError
                is IOException -> ResourceState.NetworkError
                is UnknownHostException -> ResourceState.NetworkError
                is TimeoutCancellationException -> ResourceState.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    val errorMsg = convertErrorBody(throwable)
                    ResourceState.GenericError(
                        code,
                        errorMsg
                    )
                }
                else -> {
                    ResourceState.Error(throwable.message ?: "Unknown error")
                }
            }
        }
    }
}

@Suppress("NestedBlockDepth", "SwallowedException", "TooGenericExceptionCaught")
private fun convertErrorBody(throwable: HttpException): String? {
    try {
        throwable.response()?.errorBody()?.let { responseBody ->
            val jsonObject = JSONObject(responseBody.toString())

            // If errorBody has "errors"
            if (jsonObject.has("errors")) {
                // Get error array from errorBody
                val errorJsonArray = jsonObject.getJSONArray("errors")
                // If errors array is not empty and first object of error array has "code" in it
                if (errorJsonArray.length() > 0 &&
                    errorJsonArray.getJSONObject(INDEX_OF_ERROR_CODE).has("code")
                ) {
                    // Get error code
                    // Throw exception with error code
                    return errorJsonArray.getJSONObject(INDEX_OF_ERROR_CODE).getString("code")
                }
            }
        }
    } catch (exception: Exception) {
        // Do Nothing
    }
    return null
}

@Suppress("NestedBlockDepth", "ThrowsCount")
suspend fun <T> executeOrThrow(apiCall: suspend () -> T): T? {
    var response: T? = null
    try {
        response = apiCall.invoke()
    } catch (httpException: HttpException) {
        httpException.response()?.let { httpErrorResponse ->
            val errorBody = httpErrorResponse.errorBody()
            val errorCode = httpErrorResponse.code()
            val errorJsonObject = JSONObject(errorBody.toString())
            val errorJsonArray = errorJsonObject.getJSONArray("errors")
            errorBody?.let {
                when {
                    errorCode in HTTP_ERROR_START..HTTP_ERROR_END -> throw HttpException(
                        Response.error<BaseResponse<T>>(
                            errorCode,
                            errorBody
                        )
                    )
                    hasCustomError(errorJsonObject, errorJsonArray) -> {
                        val code =
                            errorJsonArray.getJSONObject(INDEX_OF_ERROR_CODE).getString("code")
                        throw java.lang.Exception(code)
                    }
                    else -> throw java.lang.Exception(UNKNOWN_ERROR)
                }
            }
        }
    }
    return response
}

private fun hasCustomError(errorJsonObject: JSONObject, errorJsonArray: JSONArray): Boolean =
    errorJsonObject.has("errors") &&
        errorJsonArray.length() > 0 &&
        errorJsonArray.getJSONObject(INDEX_OF_ERROR_CODE).has("code")
