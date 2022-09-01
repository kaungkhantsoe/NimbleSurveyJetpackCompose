package com.kks.nimblesurveyjetpackcompose.model.response

import com.squareup.moshi.*
import retrofit2.Response

@JsonClass(generateAdapter = true)
data class BaseResponse<D>(
    @Json(name = "data") val data: D? = null,
    @Json(name = "errors") val errors: List<ErrorResponse>? = null,
    @Json(name = "meta") val meta: MetaResponse? = null,
    @Json(name = "included") val included: List<BaseIncludedResponse>? = null
)
