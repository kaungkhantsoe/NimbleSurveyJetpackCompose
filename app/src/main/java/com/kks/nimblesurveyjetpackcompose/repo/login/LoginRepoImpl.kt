package com.kks.nimblesurveyjetpackcompose.repo.login

import com.kks.nimblesurveyjetpackcompose.di.ServiceQualifier
import com.kks.nimblesurveyjetpackcompose.model.ApiInterface
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.request.LoginRequest
import com.kks.nimblesurveyjetpackcompose.model.response.LoginResponse
import com.kks.nimblesurveyjetpackcompose.util.*
import com.kks.nimblesurveyjetpackcompose.util.extensions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepoImpl @Inject constructor(
    @ServiceQualifier private val apiInterface: ApiInterface,
    private val preferenceManager: PreferenceManager,
    private val customKeyProvider: CustomKeyProvider
) : LoginRepo {

    override fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<ResourceState<LoginResponse>> = flow {
        val apiResult = safeApiCall(Dispatchers.IO) {
            executeOrThrow {
                apiInterface.loginUser(
                    LoginRequest(
                        email = email,
                        password = password,
                        clientId = customKeyProvider.getClientId(),
                        clientSecret = customKeyProvider.getClientSecret()
                    )
                )
            }
        }
        when (apiResult) {
            is ResourceState.Success -> {
                apiResult.successData?.data?.let { loginResponse ->
                    preferenceManager.setStringData(
                        PREF_ACCESS_TOKEN,
                        loginResponse.attributes?.accessToken.orEmpty()
                    )
                    preferenceManager.setStringData(
                        PREF_REFRESH_TOKEN,
                        loginResponse.attributes?.refreshToken.orEmpty()
                    )
                    emit(ResourceState.Success(loginResponse))
                } ?: emit(ResourceState.Error(SUCCESS_WITH_NULL_ERROR))
            }
            is ResourceState.Error -> emit(ResourceState.Error(apiResult.error))
            is ResourceState.GenericError -> emit(
                ResourceState.GenericError(
                    apiResult.code,
                    apiResult.error
                )
            )
            else -> {
                emit(ResourceState.NetworkError)
            }
        }
    }.catch { error ->
        emit(ResourceState.Error(error.message ?: UNKNOWN_ERROR_MESSAGE))
    }
}
