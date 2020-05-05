package com.br.queroajudar.network

import com.br.queroajudar.util.QueroAjudarPreferences
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor() : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        return response.request.newBuilder()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("Authorization", QueroAjudarPreferences.apiToken ?: "")
            .build()
    }

}