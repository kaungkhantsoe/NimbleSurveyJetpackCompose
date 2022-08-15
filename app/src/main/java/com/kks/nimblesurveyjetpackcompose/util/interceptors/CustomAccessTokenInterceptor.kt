package com.kks.nimblesurveyjetpackcompose.util.interceptors

import com.kks.nimblesurveyjetpackcompose.repo.token.TokenRepoImpl
import com.kks.nimblesurveyjetpackcompose.util.PREF_ACCESS_TOKEN
import com.kks.nimblesurveyjetpackcompose.util.PREF_REFRESH_TOKEN
import com.kks.nimblesurveyjetpackcompose.util.PreferenceManager
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection

class CustomAccessTokenInterceptor(
    private val preferenceManager: PreferenceManager,
    private val tokenRepo: Lazy<TokenRepoImpl>
) : Interceptor {

    private var isRefreshed = false

    override fun intercept(chain: Interceptor.Chain): Response {

        val accessToken: String =
            preferenceManager.getStringData(PREF_ACCESS_TOKEN) ?: ""
        val request: Request = newRequestWithAccessToken(chain.request(), accessToken)

        val response: Response = chain.proceed(request)

        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            val job = CoroutineScope(Dispatchers.IO).launch {
                tokenRepo.get().refreshToken(
                    preferenceManager.getStringData(PREF_REFRESH_TOKEN) ?: ""
                ).collectLatest {
                    isRefreshed = true
                }
            }

            synchronized(this) {
                while (!isRefreshed) {
                    // Wait for cache
                }
                isRefreshed = false
                val newAccessToken: String =
                    preferenceManager.getStringData(PREF_ACCESS_TOKEN) ?: ""
                job.cancel()
                // Access token is refreshed in another thread.
                if (accessToken != newAccessToken) {
                    return chain.proceed(newRequestWithAccessToken(request, newAccessToken))
                }
            }
        }
        return response
    }

    private fun newRequestWithAccessToken(request: Request, accessToken: String): Request =
        // Create new request with new accessToken fetched earlier
        request.newBuilder().header(
            "Authorization",
            "Bearer $accessToken"
        ).build()
}
