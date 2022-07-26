package com.kks.nimblesurveyjetpackcompose.repo.token

import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.request.RefreshTokenRequest
import com.kks.nimblesurveyjetpackcompose.model.response.LoginResponse
import com.kks.nimblesurveyjetpackcompose.network.AuthApi
import com.kks.nimblesurveyjetpackcompose.util.*
import com.kks.nimblesurveyjetpackcompose.util.extensions.SUCCESS_WITH_NULL_ERROR
import com.kks.nimblesurveyjetpackcompose.util.extensions.catchError
import com.kks.nimblesurveyjetpackcompose.util.extensions.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TokenRepoImpl @Inject constructor(
    private val apiInterface: AuthApi,
    private val preferenceManager: PreferenceManager,
    private val customKeyProvider: CustomKeyProvider
) : TokenRepo {

    override fun refreshToken(refreshToken: String): Flow<ResourceState<LoginResponse>> =
        flow {
            val apiResult = safeApiCall(Dispatchers.IO) {
                apiInterface.refreshToken(
                    RefreshTokenRequest(
                        refreshToken = refreshToken,
                        clientId = customKeyProvider.getClientId(),
                        clientSecret = customKeyProvider.getClientSecret()
                    )
                )
            }
            when (apiResult) {
                is ResourceState.Success -> {
                    apiResult.data.data?.attributes?.let { response ->
                        preferenceManager.setStringData(
                            PREF_ACCESS_TOKEN,
                            response.accessToken
                        )
                        preferenceManager.setStringData(
                            PREF_REFRESH_TOKEN,
                            response.refreshToken
                        )
                        emit(ResourceState.Success(apiResult.data.data))
                    } ?: emit(ResourceState.Error(SUCCESS_WITH_NULL_ERROR))
                }
                is ResourceState.Error -> emit(ResourceState.Error(apiResult.error))
                ResourceState.NetworkError -> emit(ResourceState.NetworkError)
                ResourceState.Loading -> emit(ResourceState.Loading)
            }
        }.catchError()
}
