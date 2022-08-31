package com.kks.nimblesurveyjetpackcompose.repo.token

import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.response.LoginResponse
import kotlinx.coroutines.flow.Flow

interface TokenRepo {
    fun refreshToken(refreshToken: String): Flow<ResourceState<LoginResponse>>
}
