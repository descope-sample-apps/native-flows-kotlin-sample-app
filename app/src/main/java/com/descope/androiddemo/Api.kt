package com.descope.androiddemo

import com.descope.Descope
import org.json.JSONObject
import java.net.URL
import java.net.URLConnection
import javax.net.ssl.HttpsURLConnection

// A class representing any number of exceptions that might
// be thrown from the network layer. In this use case, there's
// only one.
class SessionExpired: Exception()

// A class representing a very simplified network layer
class Api {

    // This function is an example call and should not be used as is.
    // It represents an API call to your backend the requires the
    // user to be authenticated
    suspend fun myApiCall(): Boolean {
        val url = URL("https://api.descope.com/v1/auth/validate")
        val response = sendRequestWithSession(url, "POST", emptyMap())
        return (response.code == HttpsURLConnection.HTTP_OK)
    }

    private suspend fun sendRequestWithSession(url: URL, method: String, body: Map<String, Any?>?): Response {
        val session = Descope.sessionManager.session ?: throw Exception("User is not logged in - cannot perform protected API")

        // refreshSessionIfNeeded will actively refresh the session only
        // if it is about to expire. Otherwise it is a no-op.
        // If it throws - the request fails and should be treated as a
        // refresh failure
        try {
            Descope.sessionManager.refreshSessionIfNeeded()
        } catch (e: Exception) {
            // refresh failed - the user must re-authenticate
            throw SessionExpired()
        }

        // perform the request
        var result = sendRequest(url, method, body, session.sessionJwt)

        // If the request is unauthorized - try to refresh
        if (result.code == HttpsURLConnection.HTTP_UNAUTHORIZED) {

            // if the refresh token is expired the user must re-authenticate
            if (session.refreshToken.isExpired) {
                throw SessionExpired()
            }

            // try to refresh
            try {
                val refreshResponse = Descope.auth.refreshSession(session.refreshJwt)
                Descope.sessionManager.updateTokens(refreshResponse)
            } catch (e: Exception) {
                // refresh failed - the user must re-authenticate
                throw SessionExpired()
            }

            // retry original request
            result = sendRequest(url, method, body, session.sessionJwt)
        }
        return result
    }


    private fun addAuthHeader(connection: URLConnection, sessionJwt: String) {
        connection.setRequestProperty("Authorization", "Bearer $descopeProjectId:$sessionJwt")
    }

    private fun sendRequest(url: URL, method: String, body: Map<String, Any?>?, sessionJwt: String): Response {
        val connection = url.openConnection() as HttpsURLConnection
        try {
            connection.requestMethod = method
            connection.setRequestProperty("Accept", "application/json")
            addAuthHeader(connection, sessionJwt)

            Descope.sessionManager.session

            // Send body if needed
            body?.run {
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true
                // Send the request
                connection.outputStream.bufferedWriter().use {
                    it.write(JSONObject(this).toString())
                    it.flush()
                }
            }

            // Return response
            val responseCode = connection.responseCode
            val responseBody = if (responseCode == HttpsURLConnection.HTTP_OK) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                connection.errorStream.bufferedReader().use { it.readText() }
            }
            println("Received: $responseCode, $responseBody")
            return Response(code = responseCode, body = responseBody)
        } finally {
            connection.disconnect()
        }
    }
}

private data class Response(
    val code: Int,
    val body: String,
)
