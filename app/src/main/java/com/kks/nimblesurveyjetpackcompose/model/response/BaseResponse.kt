package com.kks.nimblesurveyjetpackcompose.model.response

@Suppress("UnusedPrivateMember")
class BaseResponse<D> constructor(obj: D, errors: List<ErrorResponse>? = null, meta: MetaResponse? = null) {
    val data: D? = obj
    val errors: List<ErrorResponse>? = errors
    val meta: MetaResponse? = null
}
