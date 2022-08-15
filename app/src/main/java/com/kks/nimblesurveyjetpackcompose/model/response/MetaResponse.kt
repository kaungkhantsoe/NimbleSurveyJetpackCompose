package com.kks.nimblesurveyjetpackcompose.model.response

import com.google.gson.annotations.SerializedName

data class MetaResponse(
    val page: Int?,
    val pages: Int?,
    @SerializedName("page_size")
    val pageSize: Int?,
    val records: Int?,
    val message: String?
)
