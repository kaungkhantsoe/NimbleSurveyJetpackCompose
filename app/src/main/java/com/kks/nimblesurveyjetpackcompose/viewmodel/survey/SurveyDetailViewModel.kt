package com.kks.nimblesurveyjetpackcompose.viewmodel.survey

import androidx.lifecycle.ViewModel
import com.kks.nimblesurveyjetpackcompose.model.SurveyQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SurveyDetailViewModel @Inject constructor() : ViewModel() {

    private val _currentPage = MutableStateFlow(0)
    private val _surveyQuestionList = MutableStateFlow(emptyList<SurveyQuestion>())

    val currentPage: StateFlow<Int>
        get() = _currentPage.asStateFlow()

    val surveyQuestionList: StateFlow<List<SurveyQuestion>>
        get() = _surveyQuestionList.asStateFlow()

    fun setCurrentPage(pageNumber: Int) {
        _currentPage.value = pageNumber
    }
}
