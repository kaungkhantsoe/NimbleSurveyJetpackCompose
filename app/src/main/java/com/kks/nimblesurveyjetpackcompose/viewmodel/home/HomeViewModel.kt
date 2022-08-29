package com.kks.nimblesurveyjetpackcompose.viewmodel.home

import androidx.lifecycle.ViewModel
import com.kks.nimblesurveyjetpackcompose.model.ErrorModel
import com.kks.nimblesurveyjetpackcompose.model.response.SurveyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    private var _error = MutableStateFlow<ErrorModel?>(null)
    private var _surveyList = MutableStateFlow<ArrayList<SurveyResponse>>(arrayListOf())

    val error: StateFlow<ErrorModel?>
        get() = _error.asStateFlow()

    val surveyList: StateFlow<ArrayList<SurveyResponse>>
        get() = _surveyList.asStateFlow()

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()
}
