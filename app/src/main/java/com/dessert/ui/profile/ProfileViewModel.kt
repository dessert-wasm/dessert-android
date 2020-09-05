package com.dessert.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.sample.UserQuery

class ProfileViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is profile Fragment"
    }
    val text: LiveData<String> = _text
    lateinit var username: String
    lateinit var profilePicUrl: String
    lateinit var tokens: List<UserQuery.Token>
}