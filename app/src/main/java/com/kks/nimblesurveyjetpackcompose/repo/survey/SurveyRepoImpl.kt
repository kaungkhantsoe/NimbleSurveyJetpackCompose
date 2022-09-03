package com.kks.nimblesurveyjetpackcompose.repo.survey

import com.kks.nimblesurveyjetpackcompose.di.ServiceQualifier
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.response.IncludedResponse
import com.kks.nimblesurveyjetpackcompose.network.Api
import com.kks.nimblesurveyjetpackcompose.util.extensions.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SurveyRepoImpl @Inject constructor(@ServiceQualifier private val api: Api) : SurveyRepo {
    override fun getSurveyDetails(surveyId: String): Flow<ResourceState<List<IncludedResponse>>> =
        flow {
            emit(ResourceState.Loading)
            val apiResult = safeApiCall(Dispatchers.IO) { api.getSurveyDetail(surveyId = surveyId) }
            when (apiResult) {
                is ResourceState.Success -> emit(ResourceState.Success(apiResult.data.included.orEmpty()))
                is ResourceState.Error -> emit(ResourceState.Error(apiResult.error))
                else -> emit(ResourceState.NetworkError)
            }
        }.catch { error ->
            emit(ResourceState.Error(error.message.orEmpty()))
        }
}