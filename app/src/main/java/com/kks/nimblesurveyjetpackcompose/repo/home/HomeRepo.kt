package com.kks.nimblesurveyjetpackcompose.repo.home

import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.entities.Survey
import com.kks.nimblesurveyjetpackcompose.model.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface HomeRepo {
    fun fetchSurveyList(
        pageNumber: Int,
        pageSize: Int,
        getNumberOfPage: (totalPage: Int) -> Unit
    ): Flow<ResourceState<Unit>>
    fun fetchUserDetail(): Flow<ResourceState<UserResponse>>
    fun getSurveyListFromDb(): Flow<List<Survey>>
    suspend fun clearSurveyList()
}
