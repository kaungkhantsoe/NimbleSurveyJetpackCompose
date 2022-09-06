package com.kks.nimblesurveyjetpackcompose.model.response

import com.squareup.moshi.*

@JsonClass(generateAdapter = true)
data class BaseResponse<D>(
    @Json(name = "data") val data: D? = null,
    @Json(name = "errors") val errors: List<ErrorResponse>? = null,
    @Json(name = "meta") val meta: MetaResponse? = null,
    @Json(name = "included") val included: List<IncludedResponse>? = null
)
