package com.kks.nimblesurveyjetpackcompose.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.nimblesurveyjetpackcompose.di.IoDispatcher
import com.kks.nimblesurveyjetpackcompose.repo.home.HomeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepo: HomeRepo,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    fun getUserDetail() {
        viewModelScope.launch(ioDispatcher) {
            homeRepo.fetchUserDetail().collect { }
        }
    }

    fun getSurveyList(pageNumber: Int, pageSize: Int) {
        viewModelScope.launch(ioDispatcher) {
            homeRepo.fetchSurveyList(pageNumber, pageSize).collect { }
        }
    }
}
