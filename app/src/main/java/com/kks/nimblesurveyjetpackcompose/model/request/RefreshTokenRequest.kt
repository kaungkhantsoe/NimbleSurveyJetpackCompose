package com.kks.nimblesurveyjetpackcompose.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RefreshTokenRequest(
    @Json(name = "grant_type")
    val grantType: String = "refresh_token",
    @Json(name = "refresh_token")
    val refreshToken: String,
    @Json(name = "client_id")
    val clientId: String,
    @Json(name = "client_secret")
    val clientSecret: String
)
