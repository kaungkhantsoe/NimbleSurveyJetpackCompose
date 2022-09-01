package com.kks.nimblesurveyjetpackcompose.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IncludedQuestionResponse(
    @Json(name = "id") val id: String? = null,
    @Json(name = "attributes") val attributes: SurveyDetailAttributeQuestionResponse? = null,
) : BaseIncludedResponse(type = "question") {
    @JsonClass(generateAdapter = true)
    data class SurveyDetailAttributeQuestionResponse(
        @Json(name = "text") val text: String? = null,
        @Json(name = "help_text") val helpText: String? = null,
        @Json(name = "display_order") val displayOrder: Int? = null,
        @Json(name = "short_text") val shortText: String? = null,
        @Json(name = "pick") val pick: String? = null,
        @Json(name = "display_type") val displayType: String? = null,
        @Json(name = "is_mandatory") val isMandatory: Boolean? = null,
        @Json(name = "correct_answer_id") val correctAnswerId: String? = null,
        @Json(name = "facebook_profile") val facebookProfile: String? = null,
        @Json(name = "twitter_profile") val twitterProfile: String? = null,
        @Json(name = "image_url") val imageUrl: String? = null,
        @Json(name = "cover_image_url") val coverImageUrl: String? = null,
        @Json(name = "cover_image_opacity") val coverImageOpacity: Double? = null,
        @Json(name = "is_shareable_on_facebook") val isShareableOnFacebook: Boolean? = null,
        @Json(name = "is_shareable_on_twitter") val isShareableOnTwitter: Boolean? = null,
        @Json(name = "font_face") val fontFace: String? = null,
        @Json(name = "font_size") val fontSize: String? = null,
        @Json(name = "tag_list") val tagList: String? = null
    )
}
