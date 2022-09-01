package com.kks.nimblesurveyjetpackcompose.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IncludedAnswerResponse(
    @Json(name = "id") val id: String? = null,
    @Json(name = "attributes") val attributes: SurveyDetailAttributeAnswerResponse? = null,
): BaseIncludedResponse(type = "answer") {
    @JsonClass(generateAdapter = true)
    data class SurveyDetailAttributeAnswerResponse(
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
