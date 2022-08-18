package com.kks.nimblesurveyjetpackcompose.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "attributes")
    val attributes: LoginAttributesResponse?
)
