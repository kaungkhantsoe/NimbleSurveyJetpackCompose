package com.kks.nimblesurveyjetpackcompose.repo.survey

import com.kks.nimblesurveyjetpackcompose.di.ServiceQualifier
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.SurveyQuestion
import com.kks.nimblesurveyjetpackcompose.model.request.SubmitSurveyRequest
import com.kks.nimblesurveyjetpackcompose.model.response.IncludedAnswerResponse
import com.kks.nimblesurveyjetpackcompose.model.response.IncludedQuestionResponse
import com.kks.nimblesurveyjetpackcompose.model.response.toSurveyAnswer
import com.kks.nimblesurveyjetpackcompose.model.response.toSurveyQuestion
import com.kks.nimblesurveyjetpackcompose.model.sortedByDisplayOrder
import com.kks.nimblesurveyjetpackcompose.model.toSurveyQuestionRequest
import com.kks.nimblesurveyjetpackcompose.network.Api
import com.kks.nimblesurveyjetpackcompose.util.extensions.catchError
import com.kks.nimblesurveyjetpackcompose.util.extensions.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
                                    val answerList = questionResponse.relationships
                                        ?.answers
                                        ?.data
                                        ?.mapNotNull { answers[it.id]?.firstOrNull() }
                                        ?.sortedByDisplayOrder()
                                        .orEmpty()
                                    surveyQuestion.answers = answerList
                                }
                            }
                        emit(ResourceState.Success(questions.sortedByDisplayOrder()))
                    }
                }
                is ResourceState.Error -> emit(ResourceState.Error(apiResult.error))
                else -> emit(ResourceState.NetworkError)
            }
        }.catchError()

    override fun submitSurvey(surveyId: String, surveyQuestions: List<SurveyQuestion>): Flow<ResourceState<Unit>> =
        flow {
            emit(ResourceState.Loading)
            val apiResult = safeApiCall(Dispatchers.IO) {
                api.submitSurvey(
                    submitSurveyRequest = surveyQuestions.toSubmitSurveyRequest(surveyId)
                )
            }
            when (apiResult) {
                is ResourceState.Success -> emit(ResourceState.Success(Unit))
                is ResourceState.Error -> emit(ResourceState.Error(apiResult.error))
                else -> emit(ResourceState.NetworkError)
            }
        }.catchError()
}

private fun List<SurveyQuestion>.toSubmitSurveyRequest(surveyId: String): SubmitSurveyRequest =
    SubmitSurveyRequest(surveyId = surveyId, questions = this.map { it.toSurveyQuestionRequest() })
