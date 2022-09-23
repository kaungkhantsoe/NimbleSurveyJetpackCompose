package com.kks.nimblesurveyjetpackcompose.model.response

import com.kks.nimblesurveyjetpackcompose.model.Meta
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

fun MetaResponse?.toMeta() = Meta(
    page = this?.page ?: 0,
    pages = this?.pages ?: 0,
    pageSize = this?.pageSize ?: 0,
    records = this?.records ?: 0,
    message = this?.message.orEmpty()
)
