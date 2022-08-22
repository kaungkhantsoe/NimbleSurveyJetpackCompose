package com.kks.nimblesurveyjetpackcompose.ui.presentation.home

import com.kks.nimblesurveyjetpackcompose.base.BaseAndroidComposeTest
import com.kks.nimblesurveyjetpackcompose.di.RepositoryModule
import com.kks.nimblesurveyjetpackcompose.model.ResourceState
import com.kks.nimblesurveyjetpackcompose.model.response.SurveyResponse
import com.kks.nimblesurveyjetpackcompose.model.response.UserResponse
import com.kks.nimblesurveyjetpackcompose.repo.home.HomeRepo
import com.kks.nimblesurveyjetpackcompose.viewmodel.home.HomeViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

@UninstallModules(RepositoryModule::class)
@HiltAndroidTest
class HomeScreenKtTest : BaseAndroidComposeTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var homeRepo: HomeRepo

    val homeViewModel: HomeViewModel = HomeViewModel(homeRepo, Dispatchers.IO)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    class FakeHomeRepo : HomeRepo {
        override fun fetchSurveyList(
            pageNumber: Int,
            pageSize: Int
        ): Flow<ResourceState<List<SurveyResponse>>> {
            return flowOf(ResourceState.Loading)
        }

        override fun fetchUserDetail(): Flow<ResourceState<UserResponse>> {
            return flowOf(ResourceState.Loading)
        }

    }
}
