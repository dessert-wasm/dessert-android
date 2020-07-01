package com.dessert.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.UserQuery
import com.apollographql.apollo.sample.type.PaginationQueryInput
import com.dessert.R

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        val id = requireActivity().intent.extras!!.getInt("ID");
        val apolloClient = ApolloClient.builder().serverUrl("https://dev.dessert.vodka").build()



        apolloClient.query(UserQuery(id, PaginationQueryInput(true, 0, 20)))
            .enqueue(object : ApolloCall.Callback<UserQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(response: Response<UserQuery.Data>) {
                    Log.i("OUI", response.toString())
                }
            })


        //getActivity().getIntent().getExtras().getString("image")
//        val textView: TextView = root.findViewById(R.id.text_notifications)
//        profileViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }
}