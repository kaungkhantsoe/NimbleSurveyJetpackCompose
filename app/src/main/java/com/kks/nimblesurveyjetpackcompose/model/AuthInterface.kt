package com.kks.nimblesurveyjetpackcompose.model

import com.kks.nimblesurveyjetpackcompose.model.request.RefreshTokenRequest
import com.kks.nimblesurveyjetpackcompose.model.response.BaseResponse
import com.kks.nimblesurveyjetpackcompose.model.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthInterface {
    @POST("api/v1/oauth/token")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): BaseResponse<LoginResponse>
}
