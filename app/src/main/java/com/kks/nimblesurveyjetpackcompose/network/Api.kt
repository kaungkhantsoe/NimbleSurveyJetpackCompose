package com.kks.nimblesurveyjetpackcompose.network

import com.kks.nimblesurveyjetpackcompose.model.request.LoginRequest
import com.kks.nimblesurveyjetpackcompose.model.request.SubmitSurveyRequest
import com.kks.nimblesurveyjetpackcompose.model.response.BaseResponse
import com.kks.nimblesurveyjetpackcompose.model.response.LoginResponse
import com.kks.nimblesurveyjetpackcompose.model.response.SurveyResponse
import com.kks.nimblesurveyjetpackcompose.model.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @POST("api/v1/oauth/token")
    suspend fun loginUser(@Body loginRequest: LoginRequest): BaseResponse<LoginResponse>

    @GET("api/v1/me")
    suspend fun getUserDetail(): BaseResponse<UserResponse>

    @GET("api/v1/surveys")
    suspend fun getSurveyList(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") pageSize: Int,
    ): BaseResponse<List<SurveyResponse>>

    @GET("api/v1/surveys/{surveyId}")
    suspend fun getSurveyDetail(@Path("surveyId") surveyId: String): BaseResponse<SurveyResponse>

    @POST("api/v1/responses")
    suspend fun submitSurvey(@Body submitSurveyRequest: SubmitSurveyRequest): BaseResponse<Any>
}
