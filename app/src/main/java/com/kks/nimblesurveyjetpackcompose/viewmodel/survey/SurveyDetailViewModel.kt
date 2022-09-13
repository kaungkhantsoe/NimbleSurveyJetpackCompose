package com.kks.nimblesurveyjetpackcompose.viewmodel.survey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.nimblesurveyjetpackcompose.di.IoDispatcher
import com.kks.nimblesurveyjetpackcompose.model.*
import com.kks.nimblesurveyjetpackcompose.repo.survey.SurveyRepo
import com.kks.nimblesurveyjetpackcompose.util.extensions.mapError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyDetailViewModel @Inject constructor(
    private val surveyRepo: SurveyRepo,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _currentPage = MutableStateFlow(0)
    private val _surveyQuestions = MutableStateFlow(emptyList<SurveyQuestion>())
    private val _shouldShowLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<ErrorModel?>(null)
    private val _shouldShowThanks = MutableStateFlow(false)

    val error: StateFlow<ErrorModel?>
        get() = _error.asStateFlow()

    val shouldShowLoading: StateFlow<Boolean>
        get() = _shouldShowLoading.asStateFlow()

    val currentPage: StateFlow<Int>
        get() = _currentPage.asStateFlow()

    val surveyQuestions: StateFlow<List<SurveyQuestion>>
        get() = _surveyQuestions.asStateFlow()

    fun setCurrentPage(pageNumber: Int) {
        _currentPage.value = pageNumber
    }

    val shouldShowThanks: StateFlow<Boolean>
        get() = _shouldShowThanks.asStateFlow()

    fun setAnswers(questionId: String, answers: List<SurveyAnswer>) {
        val indexOfQuestion = _surveyQuestions.value.indexOfFirst { it.id == questionId }
        _surveyQuestions.value[indexOfQuestion].answers = answers
    }

    fun getSurveyQuestions(surveyId: String) {
        viewModelScope.launch(ioDispatcher) {
            surveyRepo.getSurveyDetails(surveyId).collect {
                when (it) {
                    is ResourceState.Loading -> {
                        _shouldShowLoading.value = true
                        resetError()
                    }
                    is ResourceState.Success -> {
                        _shouldShowLoading.value = false
                        _surveyQuestions.value = it.data
                        resetError()
                    }
                    else -> {
                        _shouldShowLoading.value = false
                        it.mapError()?.let { errorModel ->
                            _error.value = errorModel
                        }
                    }
                }
            }
        }
    }

    fun submitSurvey(surveyId: String) {
        if (_currentPage.value == _surveyQuestions.value.size) {
            viewModelScope.launch(ioDispatcher) {
                surveyRepo.submitSurvey(surveyId = surveyId, surveyQuestions = _surveyQuestions.value).collect {
                    when (it) {
                        is ResourceState.Loading -> {
                            _shouldShowLoading.value = true
                            resetError()
                        }
                        is ResourceState.Success -> {
                            _shouldShowLoading.value = false
                            _shouldShowThanks.value = true
                            resetError()
                        }
                        else -> {
                            _shouldShowLoading.value = false
                            it.mapError()?.let { errorModel ->
                                _error.value = errorModel
                            }
                        }
                    }
                }
            }
        }
    }

    fun resetError() {
        _error.value = null
    }
}
