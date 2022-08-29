package com.kks.nimblesurveyjetpackcompose.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kks.nimblesurveyjetpackcompose.model.SurveyModel

@Entity(tableName = "survey")
data class Survey(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "coverImageUrl") val coverImageUrl: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String
)

fun Survey.toSurveyModel() = SurveyModel(
    id = id,
    coverImageUrl = coverImageUrl,
    title = title,
    description = description
)
