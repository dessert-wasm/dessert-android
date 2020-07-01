package com.dessert.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.LoginMutation
import com.dessert.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        val apolloClient = ApolloClient.builder().serverUrl("https://dev.dessert.vodka").build()

        val response = try {
            apolloClient.mutate(LoginMutation(username, password, true)).toDeferred().await();
        } catch (e: ApolloException) {
            return Result.Error(IOException("Request login failed in", e))
        }

        if (!response.hasErrors()) {
            val id = response.data!!.login.id;

            return Result.Success(LoggedInUser(id, username))
        }
        return Result.Error(IOException("Wrong login"))
    }

    fun logout() {
        // TODO: revoke authentication
    }
}