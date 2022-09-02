package com.kks.nimblesurveyjetpackcompose.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
open class IncludedResponse {
    @Json(name = "id") val id: String? = null
    @Json(name = "type") val type: String? = null
}

@JsonClass(generateAdapter = true)
data class IncludedQuestionResponse(
    @Json(name = "attributes") val attributes: AttributeQuestionResponse? = null,
) : IncludedResponse() {
    @JsonClass(generateAdapter = true)
    data class AttributeQuestionResponse(
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

@JsonClass(generateAdapter = true)
data class IncludedAnswerResponse(
    @Json(name = "attributes") val attributes: AttributeAnswerResponse? = null,
): IncludedResponse() {
    @JsonClass(generateAdapter = true)
    data class AttributeAnswerResponse(
        @Json(name = "text") var text: String? = null,
        @Json(name = "help_text") var helpText: String? = null,
        @Json(name = "input_mask_placeholder") var inputMaskPlaceholder: String? = null,
        @Json(name = "short_text") var shortText: String? = null,
        @Json(name = "is_mandatory") var isMandatory: Boolean? = null,
        @Json(name = "is_customer_first_name") var isCustomerFirstName: Boolean? = null,
        @Json(name = "is_customer_last_name") var isCustomerLastName: Boolean? = null,
        @Json(name = "is_customer_title") var isCustomerTitle: Boolean? = null,
        @Json(name = "is_customer_email") var isCustomerEmail: Boolean? = null,
        @Json(name = "prompt_custom_answer") var promptCustomAnswer: Boolean? = null,
        @Json(name = "weight") var weight: String? = null,
        @Json(name = "display_order") var displayOrder: Int? = null,
        @Json(name = "display_type") var displayType: String? = null,
        @Json(name = "input_mask") var inputMask: String? = null,
        @Json(name = "date_constraint") var dateConstraint: String? = null,
        @Json(name = "default_value") var defaultValue: String? = null,
        @Json(name = "response_class") var responseClass: String? = null,
        @Json(name = "reference_identifier") var referenceIdentifier: String? = null,
        @Json(name = "score") var score: Int? = null,
        @Json(name = "alerts") var alerts: ArrayList<String> = arrayListOf()
    )
}
