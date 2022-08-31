package com.kks.nimblesurveyjetpackcompose.repo.login

import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.response.LoginResponse
import kotlinx.coroutines.flow.Flow

interface LoginRepo {
    fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<ResourceState<LoginResponse>>
}
