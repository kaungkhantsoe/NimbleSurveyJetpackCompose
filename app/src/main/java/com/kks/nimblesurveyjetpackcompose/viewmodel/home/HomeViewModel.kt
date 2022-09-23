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
const val DEFAULT_PAGES = 1
private const val START_SURVEY_NUMBER = 0

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepo: HomeRepo,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private var _error = MutableStateFlow<ErrorModel?>(null)
    private val _isRefreshing = MutableStateFlow(false)
    private val _surveyList = MutableStateFlow<List<Survey>>(listOf())
    private val _userAvatar = MutableStateFlow<String?>(null)
    private var _pageCount = DEFAULT_PAGES
    private var _records = DEFAULT_PAGES
    private val _selectedSurveyNumber = MutableStateFlow(START_SURVEY_NUMBER)

    init {
        getUserDetail()
        getSurveyListFromDb()
        getSurveyList()
    }

    val selectedSurveyNumber: StateFlow<Int>
        get() = _selectedSurveyNumber.asStateFlow()

    val surveyList: StateFlow<List<Survey>>
        get() = _surveyList.asStateFlow()

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    val error: StateFlow<ErrorModel?>
        get() = _error.asStateFlow()

    val userAvatar: StateFlow<String?>
        get() = _userAvatar.asStateFlow()

    fun setSelectedSurveyNumber(surveyNumber: Int) {
        _selectedSurveyNumber.value = surveyNumber
    }

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
            getSurveyList()
        }
    }

    fun clearCacheAndFetch() {
        viewModelScope.launch(ioDispatcher) {
            _pageCount = DEFAULT_PAGES
            _selectedSurveyNumber.value = START_SURVEY_NUMBER
            getSurveyList(isClearCache = true)
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

    private fun getSurveyList(isClearCache: Boolean = false) {
        val currentPage = ((if (isClearCache) START_SURVEY_NUMBER else _surveyList.value.size) / DEFAULT_PAGE_SIZE) + 1
        if ((currentPage <= _pageCount && _surveyList.value.size < _records) || isClearCache) {
            viewModelScope.launch(ioDispatcher) {
                homeRepo.fetchSurveyList(
                    pageNumber = currentPage,
                    pageSize = DEFAULT_PAGE_SIZE,
                    isClearCache = isClearCache
                ).collect { result ->
                    when (result) {
                        is ResourceState.Loading -> {
                            _isRefreshing.value = true
                            resetError()
                        }
                        is ResourceState.Success -> {
                            this@HomeViewModel._pageCount = result.data.pages
                            this@HomeViewModel._records = result.data.records
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
