package com.kks.nimblesurveyjetpackcompose.ui.presentation.splash

import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.response.LoginResponse
import com.kks.nimblesurveyjetpackcompose.repo.login.LoginRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLoginRepo : LoginRepo {
    override fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<ResourceState<LoginResponse>> {
        return if (password == VALID_PASSWORD) {
            flowOf(
                ResourceState.Success(
                    LoginResponse(
                        id = null,
                        type = null,
                        attributes = null
                    )
                )
            )
        } else {
            flowOf(ResourceState.Error(ERROR_MESSAGE))
        }
    }
}
