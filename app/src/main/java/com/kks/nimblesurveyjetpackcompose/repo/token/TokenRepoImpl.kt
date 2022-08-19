package com.kks.nimblesurveyjetpackcompose.repo.token

import com.kks.nimblesurveyjetpackcompose.model.AuthInterface
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.request.RefreshTokenRequest
import com.kks.nimblesurveyjetpackcompose.model.response.LoginResponse
import com.kks.nimblesurveyjetpackcompose.util.*
import com.kks.nimblesurveyjetpackcompose.util.extensions.SUCCESS_WITH_NULL_ERROR
import com.kks.nimblesurveyjetpackcompose.util.extensions.UNKNOWN_ERROR_MESSAGE
import com.kks.nimblesurveyjetpackcompose.util.extensions.executeOrThrow
import com.kks.nimblesurveyjetpackcompose.util.extensions.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TokenRepoImpl @Inject constructor(
    private val apiInterface: AuthInterface,
    private val preferenceManager: PreferenceManager,
    private val customKeyProvider: CustomKeyProvider
) : TokenRepo {

    override fun refreshToken(refreshToken: String): Flow<ResourceState<LoginResponse>> =
        flow {
            val apiResult = safeApiCall(Dispatchers.IO) {
                executeOrThrow {
                    apiInterface.refreshToken(
                        RefreshTokenRequest(
                            refreshToken = refreshToken,
                            clientId = customKeyProvider.getClientId(),
                            clientSecret = customKeyProvider.getClientSecret()
                        )
                    )
                }
            }
            when (apiResult) {
                is ResourceState.Success -> {
                    apiResult.successData?.data?.attributes?.let { response ->
                        preferenceManager.setStringData(
                            PREF_ACCESS_TOKEN,
                            response.accessToken.orEmpty()
                        )
                        preferenceManager.setStringData(
                            PREF_REFRESH_TOKEN,
                            response.refreshToken.orEmpty()
                        )
                        emit(ResourceState.Success(apiResult.successData.data))
                    } ?: emit(ResourceState.Error(SUCCESS_WITH_NULL_ERROR))
                }
                is ResourceState.Error -> {
                    emit(ResourceState.Error(apiResult.error))
                }
                is ResourceState.GenericError -> {
                    emit(
                        ResourceState.GenericError(
                            apiResult.code,
                            apiResult.error
                        )
                    )
                }
                ResourceState.NetworkError -> {
                    emit(ResourceState.NetworkError)
                }
                ResourceState.Loading -> emit(ResourceState.Loading)
                else -> { emit(ResourceState.NetworkError) }
            }
        }.catch { error ->
            emit(ResourceState.Error(error.message ?: UNKNOWN_ERROR_MESSAGE))
        }
}
