package com.kks.nimblesurveyjetpackcompose.repo.survey

import com.google.common.truth.Truth.assertThat
import com.kks.nimblesurveyjetpackcompose.attributeAnswerResponse
import com.kks.nimblesurveyjetpackcompose.attributeQuestionResponse
import com.kks.nimblesurveyjetpackcompose.includedAnswerResponse
import com.kks.nimblesurveyjetpackcompose.includedQuestionResponse
import com.kks.nimblesurveyjetpackcompose.model.QuestionDisplayType.DROPDOWN
import com.kks.nimblesurveyjetpackcompose.model.QuestionDisplayType.THUMBS
import com.kks.nimblesurveyjetpackcompose.model.QuestionDisplayType.SMILEY
import com.kks.nimblesurveyjetpackcompose.model.QuestionDisplayType.STARS
import com.kks.nimblesurveyjetpackcompose.model.QuestionDisplayType.NPS
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.request.SubmitSurveyRequest
import com.kks.nimblesurveyjetpackcompose.model.response.BaseResponse
import com.kks.nimblesurveyjetpackcompose.model.response.SurveyIncludedRelationshipsResponse
import com.kks.nimblesurveyjetpackcompose.model.toSurveyQuestionRequest
import com.kks.nimblesurveyjetpackcompose.network.Api
import com.kks.nimblesurveyjetpackcompose.surveyAnswer
import com.kks.nimblesurveyjetpackcompose.surveyQuestion
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SurveyRepoImplTest {

    private val api: Api = mockk()
    private lateinit var sut: SurveyRepoImpl

    @Before
    fun setup() {
        sut = SurveyRepoImpl(api)
    }

    @Test
    fun `When getSurveyDetail and return success`() = runTest {
        coEvery { api.getSurveyDetail(any()) } returns BaseResponse(
            included = listOf(
                includedQuestionResponse,
                includedAnswerResponse
            )
        )

        val expected = sut.getSurveyDetails("0").last()

        assertThat(expected).isInstanceOf(ResourceState.Success::class.java)
    }

    @Test
    fun `When getSurveyDetail and return success, question list is in ascending order`() = runTest {
        coEvery { api.getSurveyDetail(any()) } returns BaseResponse(
            included = listOf(
                includedQuestionResponse.copy(
                    attributes = attributeQuestionResponse.copy(displayOrder = 1),
                    relationships = SurveyIncludedRelationshipsResponse()
                ),
                includedQuestionResponse,
                includedAnswerResponse
            )
        )

        val expected = sut.getSurveyDetails("0").last()

        assertThat(expected).isInstanceOf(ResourceState.Success::class.java)
        val expectedSuccessData = expected as ResourceState.Success

        val firstQuestion = expectedSuccessData.data.first()
        assertThat(firstQuestion.displayOrder).isEqualTo(0)

        val secondQuestion = expectedSuccessData.data[1]
        assertThat(secondQuestion.displayOrder).isEqualTo(1)
    }

    @Test
    fun `When getSurveyDetail and return success, answer list is in ascending order`() = runTest {
        coEvery { api.getSurveyDetail(any()) } returns BaseResponse(
            included = listOf(
                includedQuestionResponse.copy(
                    attributes = attributeQuestionResponse
                ),
                includedAnswerResponse.copy(id = "1", attributes = attributeAnswerResponse.copy(displayOrder = 1)),
                includedAnswerResponse,
            )
        )

        val expected = sut.getSurveyDetails("0").last()

        assertThat(expected).isInstanceOf(ResourceState.Success::class.java)
        val expectedSuccessData = expected as ResourceState.Success

        val firstAnswer = expectedSuccessData.data.first().answers.first()
        assertThat(firstAnswer.displayOrder).isEqualTo(0)

        val secondAnswer = expectedSuccessData.data.first().answers[1]
        assertThat(secondAnswer.displayOrder).isEqualTo(1)
    }

    @Test
    fun `When getSurveyDetail and return error`() = runTest {
        coEvery { api.getSurveyDetail(any()) } throws Throwable("Error")

        val expected = sut.getSurveyDetails("0").last()

        assertThat(expected).isInstanceOf(ResourceState.Error::class.java)
    }

    @Test
    fun `When submitSurvey for Smiley question, answers with null is submitted`() = runTest {
        val smileyQuestion = surveyQuestion.copy(
            questionDisplayType = SMILEY,
            answers = listOf(surveyAnswer.copy(selected = true))
        )

        sut.submitSurvey(surveyId = "0", surveyQuestions = listOf(smileyQuestion)).collect()

        val expected = smileyQuestion.toSurveyQuestionRequest()
        assertThat(expected.answers.first().answer).isNull()

        coVerify { api.submitSurvey(SubmitSurveyRequest(surveyId = "0", questions = listOf(expected))) }
    }

    @Test
    fun `When submitSurvey for DropDown question, answers with null is submitted`() = runTest {
        val dropdownQuestion = surveyQuestion.copy(
            questionDisplayType = DROPDOWN,
            answers = listOf(surveyAnswer.copy(selected = true))
        )

        sut.submitSurvey(surveyId = "0", surveyQuestions = listOf(dropdownQuestion)).collect()

        val expected = dropdownQuestion.toSurveyQuestionRequest()
        assertThat(expected.answers.first().answer).isNull()

        coVerify { api.submitSurvey(SubmitSurveyRequest(surveyId = "0", questions = listOf(expected))) }
    }

    @Test
    fun `When submitSurvey for Thumbs question, answers with null is submitted`() = runTest {
        val thumbsQuestion = surveyQuestion.copy(
            questionDisplayType = THUMBS,
            answers = listOf(surveyAnswer.copy(selected = true))
        )

        sut.submitSurvey(surveyId = "0", surveyQuestions = listOf(thumbsQuestion)).collect()

        val expected = thumbsQuestion.toSurveyQuestionRequest()
        assertThat(expected.answers.first().answer).isNull()

        coVerify { api.submitSurvey(SubmitSurveyRequest(surveyId = "0", questions = listOf(expected))) }
    }

    @Test
    fun `When submitSurvey for Star question, answers with null is submitted`() = runTest {
        val starQuestion = surveyQuestion.copy(
            questionDisplayType = STARS,
            answers = listOf(surveyAnswer.copy(selected = true))
        )

        sut.submitSurvey(surveyId = "0", surveyQuestions = listOf(starQuestion)).collect()

        val expected = starQuestion.toSurveyQuestionRequest()
        assertThat(expected.answers.first().answer).isNull()

        coVerify { api.submitSurvey(SubmitSurveyRequest(surveyId = "0", questions = listOf(expected))) }
    }

    @Test
    fun `When submitSurvey for NPS question, answers with null is submitted`() = runTest {
        val npsQuestion = surveyQuestion.copy(
            questionDisplayType = NPS,
            answers = listOf(surveyAnswer.copy(selected = true))
        )

        sut.submitSurvey(surveyId = "0", surveyQuestions = listOf(npsQuestion)).collect()

        val expected = npsQuestion.toSurveyQuestionRequest()
        assertThat(expected.answers.first().answer).isNull()

        coVerify { api.submitSurvey(SubmitSurveyRequest(surveyId = "0", questions = listOf(expected))) }
    }
}
