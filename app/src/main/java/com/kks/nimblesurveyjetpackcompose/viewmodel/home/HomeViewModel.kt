package com.kks.nimblesurveyjetpackcompose.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.nimblesurveyjetpackcompose.di.IoDispatcher
import com.kks.nimblesurveyjetpackcompose.model.HomeUiState
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.repo.home.HomeRepo
import com.kks.nimblesurveyjetpackcompose.util.extensions.mapError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val DEFAULT_PAGE_SIZE = 5
const val DEFAULT_PAGES = 1
const val START_SURVEY_NUMBER = 0

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepo: HomeRepo,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private var _pageCount = DEFAULT_PAGES
    private var _records = DEFAULT_PAGES

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState>
        get() = _homeUiState.asStateFlow()

    init {
        getUserDetail()
        getSurveyListFromDb()
        getSurveyList()
    }

    fun setSelectedSurveyNumber(surveyNumber: Int) =
        _homeUiState.update { it.copy(selectedSurveyNumber = surveyNumber) }

    private fun getUserDetail() {
        viewModelScope.launch(ioDispatcher) {
            homeRepo.fetchUserDetail().collect { result ->
                when (result) {
                    is ResourceState.Success -> {
                        _homeUiState.update { it.copy(userAvatar = result.data.attributes?.avatarUrl) }
                        resetError()
                    }
                    else -> {
                        result.mapError()?.let { errorModel ->
                            _homeUiState.update { it.copy(error = errorModel) }
                        }
                    }
                }
            }
        }
    }

    fun getNextPage() {
        if (!_homeUiState.value.isRefreshing) {
            getSurveyList()
        }
    }

    fun clearCacheAndFetch() {
        viewModelScope.launch(ioDispatcher) {
            _pageCount = DEFAULT_PAGES
            _homeUiState.update { it.copy(selectedSurveyNumber = START_SURVEY_NUMBER) }
            getSurveyList(isClearCache = true)
        }
    }

    private fun getSurveyListFromDb() {
        viewModelScope.launch(ioDispatcher) {
            homeRepo.getSurveyListFromDb().collect { surveyList ->
                _homeUiState.update { it.copy(surveyList = surveyList, isRefreshing = surveyList.isEmpty()) }
            }
        }
    }

    private fun getSurveyList(isClearCache: Boolean = false) {
        val currentPage =
            ((if (isClearCache) START_SURVEY_NUMBER else _homeUiState.value.surveyList.size) / DEFAULT_PAGE_SIZE) + 1
        if ((currentPage <= _pageCount && _homeUiState.value.surveyList.size < _records) || isClearCache) {
            viewModelScope.launch(ioDispatcher) {
                homeRepo.fetchSurveyList(
                    pageNumber = currentPage,
                    pageSize = DEFAULT_PAGE_SIZE,
                    isClearCache = isClearCache
                ).collect { result ->
                    when (result) {
                        is ResourceState.Loading -> {
                            _homeUiState.update { it.copy(isRefreshing = true) }
                            resetError()
                        }
                        is ResourceState.Success -> {
                            _pageCount = result.data.pages
                            _records = result.data.records
                            resetError()
                        }
                        else -> {
                            _homeUiState.update { it.copy(isRefreshing = false, error = result.mapError()) }
                        }
                    }
                }
            }
        }
    }

    fun resetError() = _homeUiState.update { it.copy(error = null) }
}
