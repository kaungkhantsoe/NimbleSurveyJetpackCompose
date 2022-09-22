package com.kks.nimblesurveyjetpackcompose.repo.home

import com.kks.nimblesurveyjetpackcompose.model.Meta
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.Survey
import com.kks.nimblesurveyjetpackcompose.model.response.MetaResponse
import com.kks.nimblesurveyjetpackcompose.model.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface HomeRepo {
    fun fetchSurveyList(
        pageNumber: Int,
        pageSize: Int,
        clearCache: Boolean
    ): Flow<ResourceState<Meta>>
    fun fetchUserDetail(): Flow<ResourceState<UserResponse>>
    fun getSurveyListFromDb(): Flow<List<Survey>>
}
