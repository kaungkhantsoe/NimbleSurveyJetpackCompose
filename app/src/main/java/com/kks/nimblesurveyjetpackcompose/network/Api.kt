package com.kks.nimblesurveyjetpackcompose.network

import com.kks.nimblesurveyjetpackcompose.model.request.LoginRequest
import com.kks.nimblesurveyjetpackcompose.model.response.BaseResponse
import com.kks.nimblesurveyjetpackcompose.model.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("api/v1/oauth/token")
    suspend fun loginUser(@Body loginRequest: LoginRequest): BaseResponse<LoginResponse>
}
