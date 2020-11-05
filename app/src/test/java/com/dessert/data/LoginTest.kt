package com.dessert.data

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.LoginMutation
import com.dessert.CustomApolloClient
import org.junit.Assert
import org.junit.Test

class LoginTest {
    @Test
    fun login() {
        val client: ApolloClient = CustomApolloClient.client

        Assert.assertEquals(true, client.serverUrl.isHttps)
        Assert.assertEquals("prod.dessert.vodka", client.serverUrl.host())

        client.mutate(LoginMutation("lucas.santoni@epitech.eu", "12345", true))
            .enqueue(object : ApolloCall.Callback<LoginMutation.Data>() {
                override fun onResponse(response: Response<LoginMutation.Data>) {

                }

                override fun onFailure(e: ApolloException) {
                    assert(true)
                }
            }
            )
    }
}