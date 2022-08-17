package com.kks.nimblesurveyjetpackcompose.util.interceptors

import com.kks.nimblesurveyjetpackcompose.util.PREF_ACCESS_TOKEN
import com.kks.nimblesurveyjetpackcompose.util.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AccessTokenInterceptor(
    private val preferenceManager: PreferenceManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken: String =
            preferenceManager.getStringData(PREF_ACCESS_TOKEN).orEmpty()
        val request: Request = newRequestWithAccessToken(chain.request(), accessToken)
        return chain.proceed(request)
    }

    private fun newRequestWithAccessToken(request: Request, accessToken: String): Request =
        // Create new request with new accessToken fetched earlier
        request.newBuilder().header(
            "Authorization",
            "Bearer $accessToken"
        ).build()
}
