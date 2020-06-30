package com.dessert.data

import android.util.Log
import apollo.CustomApolloClient
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.LoginMutation
import com.dessert.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        val apolloClient = ApolloClient.builder().serverUrl("https://dev.dessert.vodka").build()

        try {


            apolloClient.mutate(LoginMutation(username, password, true))
                .enqueue(object : ApolloCall.Callback<LoginMutation.Data>() {
                    override fun onFailure(e: ApolloException) {
                        Log.e("TAG", e.message, e);
                    }

                    override fun onResponse(response: Response<LoginMutation.Data>) {
                        Log.i("TAG", response.toString());
                    }
                })
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}