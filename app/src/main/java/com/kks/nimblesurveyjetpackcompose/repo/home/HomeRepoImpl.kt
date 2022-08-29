package com.kks.nimblesurveyjetpackcompose.repo.home

import com.kks.nimblesurveyjetpackcompose.cache.SurveyDao
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.SurveyModel
import com.kks.nimblesurveyjetpackcompose.model.entities.Survey
import com.kks.nimblesurveyjetpackcompose.model.entities.toSurveyModel
import com.kks.nimblesurveyjetpackcompose.model.response.UserResponse
import com.kks.nimblesurveyjetpackcompose.model.response.toSurvey
import com.kks.nimblesurveyjetpackcompose.network.Api
import com.kks.nimblesurveyjetpackcompose.util.extensions.SUCCESS_WITH_NULL_ERROR
import com.kks.nimblesurveyjetpackcompose.util.extensions.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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
        getNumberOfPage: (totalPage: Int) -> Unit
    ): Flow<ResourceState<Unit>> = flow {
        emit(ResourceState.Loading)
        val apiResult = safeApiCall(Dispatchers.IO) { apiInterface.getSurveyList(pageNumber, pageSize) }
        when (apiResult) {
            is ResourceState.Success -> {
                apiResult.data.data?.let { surveyResponseList ->
                    surveyDao.addSurveys(surveyResponseList.map { it.toSurvey() })
                    getNumberOfPage(apiResult.data.meta?.pages ?: 0)
                }
                emit(ResourceState.Success(Unit))
            }
            is ResourceState.Error -> emit(ResourceState.Error(apiResult.error))
            else -> emit(ResourceState.NetworkError)
        }
    }.catch { error ->
        emit(ResourceState.Error(error.message.orEmpty()))
    }

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
        }.catch { error ->
            emit(ResourceState.Error(error.message.orEmpty()))
        }

    override fun getSurveyListFromDb(): Flow<List<SurveyModel>> =
        surveyDao.getSurveys().transform { value: List<Survey> -> emit(value.map { it.toSurveyModel() }) }

    override suspend fun clearSurveyList() = surveyDao.clearSurveys()
}
