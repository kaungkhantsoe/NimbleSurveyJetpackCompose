package com.kks.nimblesurveyjetpackcompose.repo.home

import com.kks.nimblesurveyjetpackcompose.model.ApiInterface
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.response.SurveyResponse
import com.kks.nimblesurveyjetpackcompose.model.response.UserResponse
import com.kks.nimblesurveyjetpackcompose.util.extensions.SUCCESS_WITH_NULL_ERROR
import com.kks.nimblesurveyjetpackcompose.util.extensions.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepoImpl @Inject constructor(
    private val apiInterface: ApiInterface
) : HomeRepo {

    override fun fetchSurveyList(
        pageNumber: Int,
        pageSize: Int
    ): Flow<ResourceState<List<SurveyResponse>>> = flow {
        val apiResult = safeApiCall(Dispatchers.IO) {
            apiInterface.getSurveyList(pageNumber, pageSize)
        }
        when (apiResult) {
            is ResourceState.Success -> {
                apiResult.successData?.data?.let {
                    emit(ResourceState.Success(it))
                } ?: emit(ResourceState.Error(SUCCESS_WITH_NULL_ERROR))
            }
            is ResourceState.Error -> emit(ResourceState.Error(apiResult.error))
            else -> {
                emit(ResourceState.NetworkError)
            }
        }
    }.catch { error ->
        emit(ResourceState.Error(error.message.orEmpty()))
    }

    override fun fetchUserDetail(): Flow<ResourceState<UserResponse>> =
        flow {
            val apiResult = safeApiCall(Dispatchers.IO) { apiInterface.getUserDetail() }
            when (apiResult) {
                is ResourceState.Success -> {
                    apiResult.successData.data?.let {
                        emit(ResourceState.Success(it))
                    } ?: emit(ResourceState.Error(SUCCESS_WITH_NULL_ERROR))
                }
                is ResourceState.Error -> emit(ResourceState.Error(apiResult.error))
                else -> {
                    emit(ResourceState.NetworkError)
                }
            }
        }.catch { error ->
            emit(ResourceState.Error(error.message.orEmpty()))
        }
}
