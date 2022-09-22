package com.kks.nimblesurveyjetpackcompose.repo.home

import com.kks.nimblesurveyjetpackcompose.cache.SurveyDao
import com.kks.nimblesurveyjetpackcompose.model.Meta
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.Survey
import com.kks.nimblesurveyjetpackcompose.model.entities.SurveyEntity
import com.kks.nimblesurveyjetpackcompose.model.entities.toSurvey
import com.kks.nimblesurveyjetpackcompose.model.response.UserResponse
import com.kks.nimblesurveyjetpackcompose.model.response.toMeta
import com.kks.nimblesurveyjetpackcompose.model.response.toSurvey
import com.kks.nimblesurveyjetpackcompose.network.Api
import com.kks.nimblesurveyjetpackcompose.util.extensions.SUCCESS_WITH_NULL_ERROR
import com.kks.nimblesurveyjetpackcompose.util.extensions.catchError
import com.kks.nimblesurveyjetpackcompose.util.extensions.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class HomeRepoImpl @Inject constructor(
    private val apiInterface: Api,
    private val surveyDao: SurveyDao
) : HomeRepo {

    override fun fetchSurveyList(
        pageNumber: Int,
        pageSize: Int,
        isClearCache: Boolean
    ): Flow<ResourceState<Meta>> = flow {
        emit(ResourceState.Loading)
        val apiResult = safeApiCall(Dispatchers.IO) { apiInterface.getSurveyList(pageNumber, pageSize) }
        when (apiResult) {
            is ResourceState.Success -> {
                apiResult.data.data?.let { surveyResponseList ->
                    if (isClearCache) surveyDao.clearSurveys()
                    surveyDao.addSurveys(surveyResponseList.map { it.toSurvey() })
                }
                emit(ResourceState.Success(apiResult.data.meta?.toMeta() ?: Meta()))
            }
            is ResourceState.Error -> emit(ResourceState.Error(apiResult.error))
            else -> emit(ResourceState.NetworkError)
        }
    }.catchError()

    override fun fetchUserDetail(): Flow<ResourceState<UserResponse>> =
        flow {
            emit(ResourceState.Loading)
            val apiResult = safeApiCall(Dispatchers.IO) { apiInterface.getUserDetail() }
            when (apiResult) {
                is ResourceState.Success -> {
                    apiResult.data.data?.let {
                        emit(ResourceState.Success(it))
                    } ?: emit(ResourceState.Error(SUCCESS_WITH_NULL_ERROR))
                }
                is ResourceState.Error -> emit(ResourceState.Error(apiResult.error))
                else -> emit(ResourceState.NetworkError)
            }
        }.catchError()

    override fun getSurveyListFromDb(): Flow<List<Survey>> =
        surveyDao.getSurveys().transform { value: List<SurveyEntity> -> emit(value.map { it.toSurvey() }) }
}
