package com.kks.nimblesurveyjetpackcompose.model.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("grant_type")
    val grantType: String = "password",
    val email: String,
    val password: String,
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("client_secret")
    val clientSecret: String
)
