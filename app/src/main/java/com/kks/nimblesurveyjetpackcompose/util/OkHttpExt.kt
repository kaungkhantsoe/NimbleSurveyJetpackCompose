@file:Suppress("ThrowsCount")

package com.kks.nimblesurveyjetpackcompose.util

import com.kks.nimblesurveyjetpackcompose.model.response.BaseResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception

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

@Suppress(
    "ThrowsCount",
    "SwallowedException",
    "RethrowCaughtException",
    "TooGenericExceptionThrown",
    "TooGenericExceptionCaught"
)
fun <T> Call<T>.executeOrThrow(): T? {
    var response: Response<T>? = null
    try {
        response = this.execute()
        val isSuccessful = response.isSuccessful
        val errorBody = response.errorBody()
        val errorCode = response.code()
        val errorJsonObject = JSONObject(errorBody.toString())
        val errorJsonArray = errorJsonObject.getJSONArray("errors")
        if (!isSuccessful && errorBody != null) {
            when {
                errorCode in HTTP_ERROR_START..HTTP_ERROR_END -> throw HttpException(
                    Response.error<BaseResponse<T>>(
                        errorCode,
                        errorBody
                    )
                )
                errorJsonObject.has("errors") &&
                    errorJsonArray.length() > 0 &&
                    errorJsonArray.getJSONObject(INDEX_OF_ERROR_CODE).has("code") -> {
                    val code = errorJsonArray.getJSONObject(INDEX_OF_ERROR_CODE).getString("code")
                    throw Exception(code)
                }
                else -> throw Exception(UNKNOWN_ERROR)
            }
        }
    } catch (httpException: HttpException) {
        response?.errorBody()?.let {
            throw HttpException(Response.error<BaseResponse<T>>(httpException.code(), it))
        }
        throw httpException
    }
    return response.body()
}
