package com.kks.nimblesurveyjetpackcompose.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MetaResponse(
    @Json(name = "page")
    val page: Int?,
    @Json(name = "pages")
    val pages: Int?,
    @Json(name = "page_size")
    val pageSize: Int?,
    @Json(name = "records")
    val records: Int?,
    @Json(name = "message")
    val message: String?
)
