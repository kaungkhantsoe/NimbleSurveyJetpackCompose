package com.kks.nimblesurveyjetpackcompose.util.interceptors

import com.kks.nimblesurveyjetpackcompose.util.PREF_ACCESS_TOKEN
import com.kks.nimblesurveyjetpackcompose.util.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AccessTokenInterceptor @Inject constructor(
    private val preferenceManager: PreferenceManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken: String =
            preferenceManager.getStringData(PREF_ACCESS_TOKEN).orEmpty()
        val request: Request = chain.request().newBuilder().header(
            "Authorization",
            "Bearer $accessToken"
        ).build()
        return chain.proceed(request)
    }
}
