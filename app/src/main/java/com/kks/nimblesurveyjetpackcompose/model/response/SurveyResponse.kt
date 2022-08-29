package com.kks.nimblesurveyjetpackcompose.model.response

import com.kks.nimblesurveyjetpackcompose.model.entities.Survey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SurveyResponse(
    @Json(name = "id")
    val id: String?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "attributes")
    val attributes: SurveyAttributesResponse?,
    @Json(name = "relationships")
    val relationships: SurveyRelationshipsResponse?
)

fun SurveyResponse.toSurvey(): Survey =
    Survey(
        id = id.orEmpty(),
        coverImageUrl = attributes?.coverImageUrl.orEmpty(),
        title = attributes?.title.orEmpty(),
        description = attributes?.description.orEmpty()
    )
