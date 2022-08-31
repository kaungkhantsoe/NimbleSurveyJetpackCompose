package com.kks.nimblesurveyjetpackcompose.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginAttributesResponse(
    @Json(name = "access_token")
    val accessToken: String?,
    @Json(name = "token_type")
    val tokenType: String?,
    @Json(name = "expires_in")
    val expiresIn: Int?,
    @Json(name = "refresh_token")
    val refreshToken: String?,
    @Json(name = "created_at")
    val createdAt: Long?
)
