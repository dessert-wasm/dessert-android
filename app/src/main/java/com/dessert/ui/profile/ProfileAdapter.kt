package com.dessert.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dessert.R
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileAdapter (var items: ArrayList<String>) : RecyclerView.Adapter<ProfileAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapter.MainHolder {
        val profileView= LayoutInflater.from(parent.context).inflate(R.layout.fragment_profile_tokens, parent, false) as View
        return MainHolder(profileView)
    }

    inner class MainHolder(val profileView: View) : RecyclerView.ViewHolder(profileView)

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val tokenNameTextView = holder.profileView.findViewById<TextView>(R.id.token_name)
        tokenNameTextView.text = items[position]
    }
}