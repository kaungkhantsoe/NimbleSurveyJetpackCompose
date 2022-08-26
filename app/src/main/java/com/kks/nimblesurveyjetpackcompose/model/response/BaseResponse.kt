package com.kks.nimblesurveyjetpackcompose.model.response

import com.squareup.moshi.*

@JsonClass(generateAdapter = true)
data class BaseResponse<D> (
    @Json(name = "data")
    val data: D?,
    @Json(name = "errors")
    val errors: List<ErrorResponse>?,
    @Json(name = "meta")
    val meta: MetaResponse?
)
