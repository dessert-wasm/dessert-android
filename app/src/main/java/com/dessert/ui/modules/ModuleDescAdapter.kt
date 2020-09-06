package com.dessert.ui.modules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.sample.SearchQuery
import com.dessert.R
import com.dessert.ui.github.GithubFragment
import me.gujun.android.taggroup.TagGroup
import java.text.SimpleDateFormat


class ModuleDescAdapter(val results: List<SearchQuery.Result?>, val fragmentManager: FragmentManager) : RecyclerView.Adapter<ModuleDescAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleDescAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val row = layoutInflater.inflate(R.layout.fragment_modules_description, parent, false)

        return ViewHolder(row)
    }

    override fun getItemCount(): Int {
        return results.size
    }

    override fun onBindViewHolder(holder: ModuleDescAdapter.ViewHolder, position: Int) {
        val result = results.get(position)
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date = sdf.parse(result!!.publishedDateTime.toString())

        sdf.applyPattern("yyyy-MM-dd");

        holder.profileView.findViewById<TextView>(R.id.module_name).text = result!!.name;
        holder.profileView.findViewById<TextView>(R.id.module_date).text = sdf.format(date);
        holder.profileView.findViewById<TextView>(R.id.module_desc).text = result!!.description;
        holder.profileView.findViewById<TextView>(R.id.module_author).text = "@" + result!!.author.nickname;

        if (result!!.tags.size > 0) {
            val list = mutableListOf<String>()
            val tagGroup = holder.profileView.findViewById<TagGroup>(R.id.tag_group)

            for (tag in result!!.tags) {
                list.add(tag.name)
            }
            tagGroup.setTags(list)
        }

        holder.profileView.setOnClickListener {
            val fragment = GithubFragment()
            val arguments = Bundle()

            arguments.putInt("id", result!!.id)
            fragment.arguments = arguments
            fragmentManager.beginTransaction().replace(R.id.fragment_module, fragment).addToBackStack(null).commit()
        }
    }

    inner class ViewHolder(val profileView: View) : RecyclerView.ViewHolder(profileView)
}