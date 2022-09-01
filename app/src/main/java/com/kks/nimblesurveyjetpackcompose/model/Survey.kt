package com.kks.nimblesurveyjetpackcompose.model

import android.os.Parcelable

data class Survey(
    val id: String,
    val coverImagePlaceholderUrl: String,
    val title: String,
    val description: String
) : Parcelable {
    val coverImageFullUrl: String
        get() = "${coverImagePlaceholderUrl}l"
}
