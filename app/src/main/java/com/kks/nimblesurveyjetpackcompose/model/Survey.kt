package com.kks.nimblesurveyjetpackcompose.model

data class Survey(
    val id: String,
    val coverImagePlaceholderUrl: String,
    val title: String,
    val description: String
) {
    val coverImageFullUrl: String
        get() = "${coverImagePlaceholderUrl}l"
}
