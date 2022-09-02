package com.kks.nimblesurveyjetpackcompose.repo.survey

import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.response.IncludedResponse
import kotlinx.coroutines.flow.Flow

interface SurveyRepo {
    fun getSurveyDetail(surveyId: String): Flow<ResourceState<List<IncludedResponse>>>
}
