package com.kks.nimblesurveyjetpackcompose.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kks.nimblesurveyjetpackcompose.model.entities.Survey
import kotlinx.coroutines.flow.Flow

@Dao
interface SurveyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSurveys(surveyList: List<Survey>)

    @Query("SELECT * FROM survey")
    fun getSurveys(): Flow<List<Survey>>

    @Query("DELETE FROM survey")
    suspend fun clearSurveys()
}
