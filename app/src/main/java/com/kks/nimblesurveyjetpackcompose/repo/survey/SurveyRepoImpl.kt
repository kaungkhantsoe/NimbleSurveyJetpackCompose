package com.kks.nimblesurveyjetpackcompose.repo.survey

import com.kks.nimblesurveyjetpackcompose.di.ServiceQualifier
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.SurveyAnswer
import com.kks.nimblesurveyjetpackcompose.model.SurveyQuestion
import com.kks.nimblesurveyjetpackcompose.model.response.IncludedAnswerResponse
import com.kks.nimblesurveyjetpackcompose.model.response.IncludedQuestionResponse
import com.kks.nimblesurveyjetpackcompose.model.response.toSurveyAnswer
import com.kks.nimblesurveyjetpackcompose.model.response.toSurveyQuestion
import com.kks.nimblesurveyjetpackcompose.network.Api
import com.kks.nimblesurveyjetpackcompose.util.extensions.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SurveyRepoImpl @Inject constructor(@ServiceQualifier private val api: Api) : SurveyRepo {
    override fun getSurveyDetails(surveyId: String): Flow<ResourceState<List<SurveyQuestion>>> =
        flow {
            emit(ResourceState.Loading)
            val apiResult = safeApiCall(Dispatchers.IO) { api.getSurveyDetail(surveyId = surveyId) }
            when (apiResult) {
                is ResourceState.Success -> {
                    apiResult.data.included?.let { includedList ->
                        val answers = includedList.filterIsInstance<IncludedAnswerResponse>()
                            .map { it.toSurveyAnswer() }
                            .groupBy { it.id }
                        val questions = includedList.filterIsInstance<IncludedQuestionResponse>()
                            .map { questionResponse ->
                                questionResponse.toSurveyQuestion().also { surveyQuestion ->
                                    val tempAnsList = arrayListOf<SurveyAnswer>()
                                    questionResponse.relationships?.answers?.data?.forEach { surveyDataResponse ->
                                        answers[surveyDataResponse.id]?.first { surveyAnswer ->
                                            tempAnsList.add(surveyAnswer)
                                        }
                                    }
                                    surveyQuestion.answers = tempAnsList
                                }
                            }
                        emit(ResourceState.Success(questions))
                    }
                }
                is ResourceState.Error -> emit(ResourceState.Error(apiResult.error))
                else -> emit(ResourceState.NetworkError)
            }
        }.catch { error ->
            emit(ResourceState.Error(error.message.orEmpty()))
        }
}
