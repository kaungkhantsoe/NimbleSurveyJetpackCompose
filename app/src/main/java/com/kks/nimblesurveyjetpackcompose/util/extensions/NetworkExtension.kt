package com.kks.nimblesurveyjetpackcompose.util.extensions

import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.util.INDEX_OF_ERROR_CODE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.ProtocolException
import java.net.UnknownHostException

private const val API_WAIT_TIME = 7000L

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
//                    val errorResponse = convertErrorBody(throwable)
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

@Suppress("NestedBlockDepth", "TooGenericExceptionCaught", "SwallowedException")
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
