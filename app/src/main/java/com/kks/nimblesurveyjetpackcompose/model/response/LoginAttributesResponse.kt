package com.kks.nimblesurveyjetpackcompose.model.response

import com.google.gson.annotations.SerializedName

data class LoginAttributesResponse(
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("token_type")
    val tokenType: String?,
    @SerializedName("expires_in")
    val expiresIn: Int?,
    @SerializedName("refresh_token")
    val refreshToken: String?,
    @SerializedName("created_at")
    val createdAt: Long?
)
