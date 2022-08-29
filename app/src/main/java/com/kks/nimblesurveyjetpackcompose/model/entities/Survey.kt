package com.kks.nimblesurveyjetpackcompose.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "survey")
data class Survey(
    @PrimaryKey
    val id: String,
    val coverImageUrl: String,
    val title: String,
    val description: String
)
