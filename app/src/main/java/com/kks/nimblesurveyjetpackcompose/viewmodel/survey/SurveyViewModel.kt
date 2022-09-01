package com.kks.nimblesurveyjetpackcompose.viewmodel.survey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.nimblesurveyjetpackcompose.di.IoDispatcher
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.repo.survey.SurveyRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor(
    private val surveyRepo: SurveyRepo,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    fun getSurveyDetail() {
        viewModelScope.launch(ioDispatcher) {
            surveyRepo.getSurveyDetail("d5de6a8f8f5f1cfe51bc").collect {

                when(it) {
                    is ResourceState.Error -> {
                        Timber.d("Survey Detail ${it.error}")
                    }
                    else -> {

                    }
                }
            }
        }
    }
}
