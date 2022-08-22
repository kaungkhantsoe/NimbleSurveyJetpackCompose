package com.kks.nimblesurveyjetpackcompose.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserAttributesResponse(
    @Json(name = "email")
    val email: String,
    @Json(name = "avatar_url")
    val avatarUrl: String
)
