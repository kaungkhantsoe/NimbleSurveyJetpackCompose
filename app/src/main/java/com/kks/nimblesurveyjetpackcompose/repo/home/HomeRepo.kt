package com.kks.nimblesurveyjetpackcompose.repo.home

import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.response.SurveyResponse
import com.kks.nimblesurveyjetpackcompose.model.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface HomeRepo {
    fun fetchSurveyList(pageNumber: Int, pageSize: Int): Flow<ResourceState<List<SurveyResponse>>>
    fun fetchUserDetail(): Flow<ResourceState<UserResponse>>
}
