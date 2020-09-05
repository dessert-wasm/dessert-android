package com.dessert.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.UserQuery
import com.apollographql.apollo.sample.type.PaginationQueryInput
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.runBlocking


import com.dessert.R
import com.dessert.ui.login.LoginActivity
import com.dessert.ui.subscription.SubscriptionActivity
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    val apolloClient = ApolloClient.builder().serverUrl("https://dev.dessert.vodka").build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)




        val logout = root.findViewById<Button>(R.id.logout)

        logout.setOnClickListener {
            //TODO: ADD LOGOUT REQUEST
            val intent = Intent(this.activity, LoginActivity::class.java)

            requireActivity().finish();
            requireActivity().startActivity(intent)
        }


        val id = requireActivity().intent.extras!!.getInt("ID");
        runBlocking {
            getUserInfos(id)
        }

        val usernameTextView = root.findViewById<TextView>(R.id.profile_username)
        usernameTextView.text = profileViewModel.username
        val profilePicImageView = root.findViewById<ImageView>(R.id.profile_picture)
        Picasso.get().load(profileViewModel.profilePicUrl).into(profilePicImageView)
        val tokensRecyclerView = root.findViewById<RecyclerView>(R.id.tokens_list)
        tokensRecyclerView.layoutManager = LinearLayoutManager(context)

        val tokens = ArrayList<String>()
        for (token in profileViewModel.tokens) {
            val str = token.token.toString() + " : " + token.description.toString()
            tokens.add(str)
        }

        tokensRecyclerView.layoutManager = LinearLayoutManager(context)
        tokensRecyclerView.adapter = ProfileAdapter(tokens)
        val itemDecor = DividerItemDecoration(context, 1) // get Orientation ... to change
        tokensRecyclerView.addItemDecoration(itemDecor)

        return root
    }

    suspend fun getUserInfos(id: Int): Response<UserQuery.Data> {
        return suspendCoroutine { continuation ->
            apolloClient.query(UserQuery(id, PaginationQueryInput(true, 0, 20)))
                .enqueue(object : ApolloCall.Callback<UserQuery.Data>() {
                    override fun onFailure(e: ApolloException) {
                        TODO("Not yet implemented")
                        continuation.resumeWithException(e)
                    }

                    override fun onResponse(response: Response<UserQuery.Data>) {
                        profileViewModel.username = "@" + response.data?.user?.nickname.toString()
                        profileViewModel.profilePicUrl = response.data?.user?.profilePicUrl.toString()
                        profileViewModel.tokens = response.data?.user?.tokens!!
                        continuation.resume(response)
                    }
                })
        }
    }
}