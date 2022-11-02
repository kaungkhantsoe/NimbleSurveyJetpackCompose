package com.kks.nimblesurveyjetpackcompose.model

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.kks.nimblesurveyjetpackcompose.util.extensions.ErrorType
import com.kks.nimblesurveyjetpackcompose.viewmodel.home.START_SURVEY_NUMBER

data class HomeUiState(
    val surveyList: List<Survey> = emptyList(),
    val userAvatar: String? = null,
    val error: ErrorModel? = null,
    val isRefreshing: Boolean = false,
    val selectedSurveyNumber: Int = START_SURVEY_NUMBER
)

class HomeUiStatePreviewParameterProvider : PreviewParameterProvider<HomeUiState> {
    override val values = sequenceOf(
        HomeUiState(
            surveyList = listOf(
                Survey("", "", "Title1", "Description1"),
                Survey("", "", "Title2", "Description2"),
            )
        ),
        HomeUiState(
            surveyList = listOf(
                Survey("", "", "Title1", "Description1"),
                Survey("", "", "Title2", "Description2"),
            ),
            error = ErrorModel(ErrorType.INFO, "Error")
        ),
        HomeUiState(error = ErrorModel(ErrorType.INFO, "Error")),
        HomeUiState(isRefreshing = true)
    )
}
