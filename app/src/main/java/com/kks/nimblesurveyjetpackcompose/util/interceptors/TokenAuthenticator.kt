package com.kks.nimblesurveyjetpackcompose.util.interceptors

import com.kks.nimblesurveyjetpackcompose.repo.token.TokenRepo
import com.kks.nimblesurveyjetpackcompose.util.PREF_ACCESS_TOKEN
import com.kks.nimblesurveyjetpackcompose.util.PREF_REFRESH_TOKEN
import com.kks.nimblesurveyjetpackcompose.util.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.lang.Exception

/*
Reference: http://sangsoonam.github.io/2019/03/06/okhttp-how-to-refresh-access-token-efficiently.html
 */
@Suppress("ReturnCount", "TooGenericExceptionCaught", "SwallowedException")
class TokenAuthenticator(
    private val preferenceManager: PreferenceManager,
    private val tokenRepo: TokenRepo,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        var isRefreshed = false

        try {
            if (!hasValidHeader(response) ||
                preferenceManager.getStringData(PREF_ACCESS_TOKEN).isNullOrEmpty()
            ) {
                return null
            }

            // Request new token with refreshToken
            val job = CoroutineScope(Dispatchers.IO).launch {
                tokenRepo.refreshToken(
                    preferenceManager.getStringData(PREF_REFRESH_TOKEN).orEmpty()
                ).collectLatest {
                    isRefreshed = true
                }
            }

            synchronized(this) {
                while (!isRefreshed) {
                    // Wait for cache
                }
                isRefreshed = false
                // Create new request with new accessToken fetched earlier
                val newToken = preferenceManager.getStringData(PREF_ACCESS_TOKEN)
                job.cancel()
                return response.request.newBuilder().header(
                    "Authorization",
                    "Bearer $newToken"
                ).build()
            }
        } catch (e: Exception) {
            return null
        }
    }

    // TODO: Improve code to retry only a certain number of times
    private fun hasValidHeader(response: Response): Boolean {
        val header = response.request.header("Authorization")
        return header != null && header.startsWith("Bearer")
    }
}
