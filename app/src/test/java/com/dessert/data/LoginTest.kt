package com.dessert.data

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.LoginMutation
import com.apollographql.apollo.sample.UserQuery
import com.apollographql.apollo.sample.type.PaginationQueryInput
import com.dessert.CustomApolloClient
import org.junit.Assert.*
import org.junit.Test

class LoginTest {
    @Test
    fun login() {
        val client: ApolloClient = CustomApolloClient.client

        assertEquals(true, client.serverUrl.isHttps)
        assertEquals("prod.dessert.vodka", client.serverUrl.host())

        client.mutate(LoginMutation("lucas.santoni@epitech.eu", "12345", true))
            .enqueue(object : ApolloCall.Callback<LoginMutation.Data>() {
                override fun onResponse(response: Response<LoginMutation.Data>) {
                    val id = response.data!!.login.id;

                    assertEquals(true, id > 0)

                    CustomApolloClient.client.query(
                        UserQuery(
                            id,
                            PaginationQueryInput(true, 0, 20)
                        )
                    )
                        .enqueue(object : ApolloCall.Callback<UserQuery.Data>() {
                            override fun onFailure(e: ApolloException) {
                                assert(true)
                            }

                            override fun onResponse(response: Response<UserQuery.Data>) {
                                assertEquals("Lucas", response.data?.user?.nickname)
                                assert(response.data?.user?.profilePicUrl.toString().isEmpty())
                            }
                        })


                }

                override fun onFailure(e: ApolloException) {
                    assert(true)
                }
            }
            )
    }
}