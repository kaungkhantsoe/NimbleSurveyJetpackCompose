package com.kks.nimblesurveyjetpackcompose.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kks.nimblesurveyjetpackcompose.model.entities.Survey

const val DB_NAME = "survey_database"

@Database(
    entities = [Survey::class],
    version = 1,
    exportSchema = true
)
abstract class SurveyDatabase : RoomDatabase() {

    abstract fun surveyDao(): SurveyDao
}
