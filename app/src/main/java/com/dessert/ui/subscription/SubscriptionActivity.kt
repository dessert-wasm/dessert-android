package com.dessert.ui.subscription

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.RegisterMutation

import com.dessert.R

class SubscriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription)

        val email = findViewById<EditText>(R.id.register_email)
        val username = findViewById<EditText>(R.id.register_username);
        val password = findViewById<EditText>(R.id.register_password);
        val register = findViewById<Button>(R.id.signup);

        register.setOnClickListener {
            if (!email.text.isEmpty() && !username.text.isEmpty() && !password.text.isEmpty()) {
                val apolloClient = ApolloClient.builder().serverUrl("https://dev.dessert.vodka").build()

                apolloClient.mutate(
                    RegisterMutation(email = email.text.toString(), nickname = username.text.toString(), password = password.text.toString())
                )
                    .enqueue(object : ApolloCall.Callback<RegisterMutation.Data>() {
                        override fun onFailure(e: ApolloException) {
                            TODO("Not yet implemented")
                        }

                        override fun onResponse(response: Response<RegisterMutation.Data>) {
                            intent.putExtra("ID", response.data!!.register.id);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    })
            }
        }
    }
}

