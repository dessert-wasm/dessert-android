package com.dessert.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.UserQuery
import com.apollographql.apollo.sample.type.PaginationQueryInput
import com.dessert.CustomApolloClient
import com.dessert.R

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val modulesRecyclerView = root.findViewById<RecyclerView>(R.id.modules_list)
        modulesRecyclerView.layoutManager = LinearLayoutManager(context)

        val id = requireActivity().intent.extras!!.getInt("ID");

        lifecycleScope.launchWhenResumed {
            val response = try {
                CustomApolloClient.client.query(UserQuery(id, PaginationQueryInput(true, 0, 50))).toDeferred().await()
            } catch (e: ApolloException) {
                Log.i("Home", "Failure", e)
                null
            }
            modulesRecyclerView.adapter = HomeDescAdapter(response!!.data!!.user.modules.result!!, childFragmentManager)
        }
        return root
    }
}