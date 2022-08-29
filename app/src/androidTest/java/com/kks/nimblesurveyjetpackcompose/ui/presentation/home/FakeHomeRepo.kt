package com.kks.nimblesurveyjetpackcompose.ui.presentation.home

import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.SurveyModel
import com.kks.nimblesurveyjetpackcompose.model.entities.Survey
import com.kks.nimblesurveyjetpackcompose.model.response.SurveyResponse
import com.kks.nimblesurveyjetpackcompose.model.response.UserResponse
import com.kks.nimblesurveyjetpackcompose.repo.home.HomeRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeHomeRepo : HomeRepo {
    override fun fetchSurveyList(
        pageNumber: Int,
        pageSize: Int,
        getNumberOfPage: (totalPage: Int) -> Unit
    ): Flow<ResourceState<List<SurveyResponse>>> {
        return flowOf(ResourceState.Loading)
    }

    override fun fetchUserDetail(): Flow<ResourceState<UserResponse>> {
        return flowOf(ResourceState.Loading)
    }

    override fun getSurveyListFromDb(): Flow<List<SurveyModel>> {
        return flowOf(listOf())
    }

    override suspend fun clearSurveyList() {
        // Do nothing
    }
}
