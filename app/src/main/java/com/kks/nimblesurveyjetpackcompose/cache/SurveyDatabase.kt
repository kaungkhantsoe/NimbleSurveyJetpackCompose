package com.kks.nimblesurveyjetpackcompose.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kks.nimblesurveyjetpackcompose.model.entities.SurveyEntity

const val DB_NAME = "survey_database"

@Database(
    entities = [SurveyEntity::class],
    version = 1,
    exportSchema = true
)
abstract class SurveyDatabase : RoomDatabase() {

    abstract fun surveyDao(): SurveyDao
}
