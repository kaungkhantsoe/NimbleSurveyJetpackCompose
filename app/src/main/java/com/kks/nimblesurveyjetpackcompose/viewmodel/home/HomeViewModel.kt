package com.kks.nimblesurveyjetpackcompose.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.nimblesurveyjetpackcompose.di.IoDispatcher
import com.kks.nimblesurveyjetpackcompose.model.ErrorModel
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.Survey
import com.kks.nimblesurveyjetpackcompose.repo.home.HomeRepo
import com.kks.nimblesurveyjetpackcompose.util.extensions.mapError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val DEFAULT_PAGE_SIZE = 5
const val DEFAULT_NUMBER_OF_PAGE = 1
const val DEFAULT_CURRENT_PAGE = 1

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepo: HomeRepo,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private var _error = MutableStateFlow<ErrorModel?>(null)
    private val _isRefreshing = MutableStateFlow(false)
    private val _surveyList = MutableStateFlow<List<Survey>>(listOf())
    private val _currentPageNumber = MutableStateFlow(DEFAULT_CURRENT_PAGE)
    private val _userAvatar = MutableStateFlow<String?>(null)
    private var totalNumberOfPage = DEFAULT_NUMBER_OF_PAGE

    init {
        getUserDetail()
        getSurveyListFromDb()
        getSurveyList()
    }

    val surveyList: StateFlow<List<Survey>>
        get() = _surveyList.asStateFlow()

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    val error: StateFlow<ErrorModel?>
        get() = _error.asStateFlow()

    val userAvatar: StateFlow<String?>
        get() = _userAvatar.asStateFlow()

    private fun getUserDetail() {
        viewModelScope.launch(ioDispatcher) {
            homeRepo.fetchUserDetail().collect { result ->
                when (result) {
                    is ResourceState.Success -> {
                        _userAvatar.value = result.data.attributes?.avatarUrl
                        resetError()
                    }
                    else -> {
                        result.mapError()?.let { errorModel ->
                            _error.value = errorModel
                        }
                    }
                }
            }
        }
    }

    fun getNextPage() {
        if (!_isRefreshing.value) {
            _currentPageNumber.value += 1
            getSurveyList()
        }
    }

    fun clearCacheAndFetch() {
        viewModelScope.launch(ioDispatcher) {
            homeRepo.clearSurveyList()
            _currentPageNumber.value = DEFAULT_CURRENT_PAGE
            totalNumberOfPage = DEFAULT_NUMBER_OF_PAGE
            getSurveyListFromDb()
            getSurveyList()
        }
    }

    private fun getSurveyListFromDb() {
        viewModelScope.launch(ioDispatcher) {
            homeRepo.getSurveyListFromDb().collect {
                _surveyList.value = it
                _isRefreshing.value = _surveyList.value.isEmpty()
            }
        }
    }

    private fun getSurveyList() {
        if (_currentPageNumber.value <= totalNumberOfPage) {
            viewModelScope.launch(ioDispatcher) {
                homeRepo.fetchSurveyList(
                    pageNumber = _currentPageNumber.value,
                    pageSize = DEFAULT_PAGE_SIZE,
                    getNumberOfPage = { totalNumberOfPage = it }
                ).collect { result ->
                    when (result) {
                        is ResourceState.Loading -> {
                            _isRefreshing.value = true
                            resetError()
                        }
                        is ResourceState.Success -> {
                            _isRefreshing.value = false
                            resetError()
                        }
                        else -> {
                            _isRefreshing.value = false
                            result.mapError()?.let { errorModel ->
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
