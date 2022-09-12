package com.kks.nimblesurveyjetpackcompose.repo.survey

import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.SurveyQuestion
import kotlinx.coroutines.flow.Flow

interface SurveyRepo {
    fun getSurveyDetails(surveyId: String): Flow<ResourceState<List<SurveyQuestion>>>
    fun submitSurvey(surveyId: String, surveyQuestions: List<SurveyQuestion>): Flow<ResourceState<Unit>>
}
